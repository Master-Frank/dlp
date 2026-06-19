/*
 * ulp-support - ULP support library
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.support.security.mfa;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Phase-3 second-factor verification service.
 *
 * <p>The pipeline (see {@link MfaChallengeOutcome} for the failure taxonomy):
 * <ol>
 *   <li>peek the pending entry from {@link MfaPendingAuthenticationStore} — absent ⇒
 *       {@link MfaChallengeOutcome#CHALLENGE_EXPIRED}
 *   <li>derive (userType, userId) from {@link Authentication#getPrincipal()} — non-{@link UserDetails}
 *       principals ⇒ {@link MfaChallengeOutcome#CHALLENGE_SESSION_INVALID}
 *   <li>consult {@link MfaLockoutService#isLockedOut(String, String)} — if locked, return
 *       {@link MfaChallengeOutcome#LOCKED_OUT} and <b>leave the pending entry alone</b> so
 *       the user can resume after the lockout window without re-doing primary auth
 *   <li>{@code /24} same-subnet check against the IP captured at primary-auth time —
 *       mismatch ⇒ delete the pending entry and return
 *       {@link MfaChallengeOutcome#CHALLENGE_SESSION_INVALID}
 *   <li>route to the right {@link MfaService} bean via the injected
 *       {@code Map<subjectType,MfaService>}; missing route ⇒ delete the pending entry and
 *       return {@link MfaChallengeOutcome#CHALLENGE_SESSION_INVALID}
 *   <li>{@code loadActiveCipher} ⇒ if null the subject lost its binding between primary auth
 *       and challenge; delete pending and return {@link MfaChallengeOutcome#SUBJECT_NOT_BOUND}
 *   <li>decrypt the cipher and verify the code with {@link MfaCodeVerifier} — failure ⇒
 *       {@link MfaLockoutService#recordFailure(String, String)} and return
 *       {@link MfaChallengeOutcome#INVALID_OTP}; the pending entry is kept so the user can
 *       retry within the 5-minute TTL
 *   <li>success ⇒ atomically {@link MfaPendingAuthenticationStore#consume(String) consume}
 *       the pending entry, clear the lockout counter, install the
 *       {@link Authentication} into the {@link SecurityContextHolder}, and persist it via
 *       the {@link SecurityContextRepository} so the session is now considered fully
 *       authenticated.
 * </ol>
 *
 * <p>This class is deployable-agnostic: console and portal wire their own {@code @Bean}
 * instance with the same constructor, supplying only their own concrete
 * {@link MfaService} beans through the {@link Collection} (admin in console, user in
 * portal).
 *
 * <p>Backup-code verification (Phase 5) is intentionally NOT implemented here — the
 * controller layer will branch on {@code code} vs {@code backupCode} request fields and
 * call a sibling service then. Adding backup-code support here later means widening this
 * service's surface, not editing the existing OTP flow.
 */
public class MfaChallengeService {

    private static final Logger                 log = LoggerFactory
        .getLogger(MfaChallengeService.class);

    private final MfaPendingAuthenticationStore pendingStore;
    private final MfaLockoutService             lockoutService;
    private final MfaCodeVerifier               codeVerifier;
    private final MfaSecretCipher               secretCipher;
    private final Map<String, MfaService>       servicesByType;
    private final SecurityContextRepository     securityContextRepository;
    private final MfaBackupCodeService          backupCodeService;

    /**
     * Test-only / legacy constructor — Phase-3 wiring without the backup-code service. Real
     * deployables MUST use the 6-arg constructor so the {@code backupCode} path resolves; the
     * Phase-3 ITs that exercise OTP only still pass {@code null} via this overload.
     */
    public MfaChallengeService(MfaPendingAuthenticationStore pendingStore,
                               MfaLockoutService lockoutService, MfaCodeVerifier codeVerifier,
                               MfaSecretCipher secretCipher, Collection<MfaService> services) {
        this(pendingStore, lockoutService, codeVerifier, secretCipher, services, null,
            new HttpSessionSecurityContextRepository());
    }

    public MfaChallengeService(MfaPendingAuthenticationStore pendingStore,
                               MfaLockoutService lockoutService, MfaCodeVerifier codeVerifier,
                               MfaSecretCipher secretCipher, Collection<MfaService> services,
                               MfaBackupCodeService backupCodeService) {
        this(pendingStore, lockoutService, codeVerifier, secretCipher, services, backupCodeService,
            new HttpSessionSecurityContextRepository());
    }

    public MfaChallengeService(MfaPendingAuthenticationStore pendingStore,
                               MfaLockoutService lockoutService, MfaCodeVerifier codeVerifier,
                               MfaSecretCipher secretCipher, Collection<MfaService> services,
                               MfaBackupCodeService backupCodeService,
                               SecurityContextRepository securityContextRepository) {
        this.pendingStore = Objects.requireNonNull(pendingStore, "pendingStore");
        this.lockoutService = Objects.requireNonNull(lockoutService, "lockoutService");
        this.codeVerifier = Objects.requireNonNull(codeVerifier, "codeVerifier");
        this.secretCipher = Objects.requireNonNull(secretCipher, "secretCipher");
        this.securityContextRepository = Objects.requireNonNull(securityContextRepository,
            "securityContextRepository");
        this.backupCodeService = backupCodeService;
        Objects.requireNonNull(services, "services");
        this.servicesByType = services.stream()
            .collect(Collectors.toUnmodifiableMap(MfaService::subjectType, Function.identity()));
    }

    /**
     * Verify the submitted TOTP code for the pending challenge identified by
     * {@code challengeId} (typically read from the {@code ulp-mfa-pending} cookie), and on
     * success commit the parked {@link Authentication} into the session.
     *
     * <p>The caller is responsible for translating the outcome into HTTP semantics:
     * <ul>
     *   <li>{@link MfaChallengeOutcome#SUCCESS} → 200 OK
     *   <li>{@link MfaChallengeOutcome#LOCKED_OUT} → 423 Locked + {@code Retry-After}
     *       (read from {@link MfaLockoutService#remainingSeconds}); the pending cookie
     *       MUST NOT be cleared so the user can retry once the window expires.
     *   <li>{@link MfaChallengeOutcome#CHALLENGE_EXPIRED},
     *       {@link MfaChallengeOutcome#CHALLENGE_SESSION_INVALID},
     *       {@link MfaChallengeOutcome#SUBJECT_NOT_BOUND} → 401/403 plus clear the
     *       pending cookie; the user must re-do primary auth.
     *   <li>{@link MfaChallengeOutcome#INVALID_OTP} → 401 keeping the pending cookie so
     *       the user can retry within the 5-minute TTL.
     * </ul>
     *
     * @param challengeId UUID minted at primary-auth time by
     *                    {@link MfaPendingAuthenticationStore#stash(Authentication, String)}
     * @param code        the 6-digit TOTP code submitted by the user
     * @param request     used for /24 IP comparison
     * @param response    used to persist the committed {@link Authentication} via the
     *                    {@link SecurityContextRepository}
     * @return the single-axis outcome — never {@code null}
     */
    public MfaChallengeOutcome verifyAndCommit(String challengeId, String code,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(response, "response");
        if (challengeId == null || challengeId.isBlank() || code == null || code.isBlank()) {
            return MfaChallengeOutcome.CHALLENGE_EXPIRED;
        }
        Optional<MfaPendingEntry> entryOpt = pendingStore.peek(challengeId);
        if (entryOpt.isEmpty()) {
            return MfaChallengeOutcome.CHALLENGE_EXPIRED;
        }
        MfaPendingEntry entry = entryOpt.get();
        Authentication pendingAuthentication = entry.getAuthentication();
        if (pendingAuthentication == null
            || !(pendingAuthentication.getPrincipal() instanceof UserDetails userDetails)) {
            pendingStore.delete(challengeId);
            log.warn("MFA challenge {} carried a non-UserDetails principal — invalidating",
                challengeId);
            return MfaChallengeOutcome.CHALLENGE_SESSION_INVALID;
        }
        UserType userType = userDetails.getUserType();
        if (userType == null || userType.getType() == null || userDetails.getId() == null) {
            pendingStore.delete(challengeId);
            return MfaChallengeOutcome.CHALLENGE_SESSION_INVALID;
        }
        String userTypeKey = userType.getType();
        String userId = userDetails.getId();

        if (lockoutService.isLockedOut(userTypeKey, userId)) {
            return MfaChallengeOutcome.LOCKED_OUT;
        }

        if (!sameIpv4Subnet(entry.getSourceIp(), request.getRemoteAddr())) {
            pendingStore.delete(challengeId);
            log.info("MFA challenge {} rejected: IP changed from {} to {}", challengeId,
                entry.getSourceIp(), request.getRemoteAddr());
            return MfaChallengeOutcome.CHALLENGE_SESSION_INVALID;
        }

        MfaService mfaService = servicesByType.get(userTypeKey);
        if (mfaService == null) {
            pendingStore.delete(challengeId);
            log.warn("No MfaService bean registered for userType {} — invalidating challenge {}",
                userTypeKey, challengeId);
            return MfaChallengeOutcome.CHALLENGE_SESSION_INVALID;
        }

        String cipher = mfaService.loadActiveCipher(userId);
        if (cipher == null) {
            pendingStore.delete(challengeId);
            return MfaChallengeOutcome.SUBJECT_NOT_BOUND;
        }

        String secretBase32;
        try {
            secretBase32 = new String(secretCipher.decrypt(cipher), StandardCharsets.UTF_8);
        } catch (RuntimeException e) {
            pendingStore.delete(challengeId);
            log.error("Failed to decrypt MFA cipher for {}:{}", userTypeKey, userId, e);
            return MfaChallengeOutcome.CHALLENGE_SESSION_INVALID;
        }

        if (!codeVerifier.isValid(secretBase32, code)) {
            long count = lockoutService.recordFailure(userTypeKey, userId);
            if (count >= lockoutService.threshold()) {
                return MfaChallengeOutcome.LOCKED_OUT;
            }
            return MfaChallengeOutcome.INVALID_OTP;
        }

        Optional<MfaPendingEntry> consumed = pendingStore.consume(challengeId);
        if (consumed.isEmpty()) {
            // Lost the race against TTL or another tab — treat as expired rather than
            // committing a stale Authentication that the user can no longer reproduce.
            return MfaChallengeOutcome.CHALLENGE_EXPIRED;
        }
        commitAuthentication(consumed.get().getAuthentication(), request, response);
        lockoutService.clear(userTypeKey, userId);
        return MfaChallengeOutcome.SUCCESS;
    }

    /**
     * Backup-code sibling of {@link #verifyAndCommit}: walks the same lockout / IP / pending
     * pipeline, but matches the submitted code against the hashed {@code backup_codes_json}
     * list instead of recomputing TOTP. On success the matched entry is removed and the
     * remaining count is returned to the caller so the controller can flag
     * {@code regenerate_backup_codes_warning} (≤2) or {@code regenerate_backup_codes_required}
     * (=0).
     *
     * <p>Lockout behaviour mirrors OTP: any non-matching code (including the case where the
     * subject has zero codes left) increments the same {@code ULP_MFA_FAIL:{userType}:{userId}}
     * counter — TOTP attempts and backup-code attempts SHARE the brute-force budget so a
     * mixed retry pattern can't bypass the threshold.
     *
     * @return outcome + post-consume remaining count (always {@code 0} for non-SUCCESS).
     *         Returns {@code (CHALLENGE_SESSION_INVALID, 0)} when the deployable forgot to
     *         wire {@link MfaBackupCodeService} (the legacy 5-arg constructor path).
     */
    public BackupCodeChallengeResult verifyBackupCodeAndCommit(String challengeId,
                                                               String backupCode,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(response, "response");
        if (backupCodeService == null) {
            log.warn("Backup-code path invoked but MfaBackupCodeService is not wired — "
                     + "treating as session-invalid. Check SecurityConfiguration bean wiring.");
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_SESSION_INVALID, 0);
        }
        if (challengeId == null || challengeId.isBlank() || backupCode == null
            || backupCode.isBlank()) {
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_EXPIRED, 0);
        }
        Optional<MfaPendingEntry> entryOpt = pendingStore.peek(challengeId);
        if (entryOpt.isEmpty()) {
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_EXPIRED, 0);
        }
        MfaPendingEntry entry = entryOpt.get();
        Authentication pendingAuthentication = entry.getAuthentication();
        if (pendingAuthentication == null
            || !(pendingAuthentication.getPrincipal() instanceof UserDetails userDetails)) {
            pendingStore.delete(challengeId);
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_SESSION_INVALID, 0);
        }
        UserType userType = userDetails.getUserType();
        if (userType == null || userType.getType() == null || userDetails.getId() == null) {
            pendingStore.delete(challengeId);
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_SESSION_INVALID, 0);
        }
        String userTypeKey = userType.getType();
        String userId = userDetails.getId();

        if (lockoutService.isLockedOut(userTypeKey, userId)) {
            return new BackupCodeChallengeResult(MfaChallengeOutcome.LOCKED_OUT, 0);
        }

        if (!sameIpv4Subnet(entry.getSourceIp(), request.getRemoteAddr())) {
            pendingStore.delete(challengeId);
            log.info("MFA backup-code challenge {} rejected: IP changed from {} to {}", challengeId,
                entry.getSourceIp(), request.getRemoteAddr());
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_SESSION_INVALID, 0);
        }

        MfaService mfaService = servicesByType.get(userTypeKey);
        if (mfaService == null) {
            pendingStore.delete(challengeId);
            log.warn(
                "No MfaService bean registered for userType {} — invalidating backup challenge {}",
                userTypeKey, challengeId);
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_SESSION_INVALID, 0);
        }
        if (mfaService.loadActiveCipher(userId) == null) {
            pendingStore.delete(challengeId);
            return new BackupCodeChallengeResult(MfaChallengeOutcome.SUBJECT_NOT_BOUND, 0);
        }

        MfaBackupCodeService.BackupCodeConsumption consumption = backupCodeService
            .consume(userTypeKey, userId, backupCode);
        if (!consumption.consumed()) {
            long count = lockoutService.recordFailure(userTypeKey, userId);
            if (count >= lockoutService.threshold()) {
                return new BackupCodeChallengeResult(MfaChallengeOutcome.LOCKED_OUT, 0);
            }
            return new BackupCodeChallengeResult(MfaChallengeOutcome.INVALID_BACKUP_CODE, 0);
        }

        Optional<MfaPendingEntry> consumed = pendingStore.consume(challengeId);
        if (consumed.isEmpty()) {
            // Lost the race against TTL — the backup code was already removed from the
            // subject's list (consume-once), so treat the resubmission as expired rather
            // than rolling the encoded JSON back. Operator-impact is minimal: one code
            // burned to discover an expired session.
            return new BackupCodeChallengeResult(MfaChallengeOutcome.CHALLENGE_EXPIRED, 0);
        }
        commitAuthentication(consumed.get().getAuthentication(), request, response);
        lockoutService.clear(userTypeKey, userId);
        return new BackupCodeChallengeResult(MfaChallengeOutcome.SUCCESS, consumption.remaining());
    }

    /**
     * Outcome wrapper for {@link #verifyBackupCodeAndCommit}: when {@code outcome == SUCCESS},
     * {@code remaining} is the post-consume count of unused backup codes (0 → forced
     * regenerate, ≤2 → warning).
     */
    public record BackupCodeChallengeResult(MfaChallengeOutcome outcome, int remaining) {
    }

    /**
     * Build the in-memory snapshot of {@code subjectType → MfaService} that the controller
     * tier can read for diagnostics. Exposes a defensive copy.
     */
    public Map<String, MfaService> registeredServices() {
        return new HashMap<>(servicesByType);
    }

    /**
     * Peek the parked {@link Authentication} for a pending challenge without consuming the
     * pending entry — used by the controller tier to build an {@link
     * cn.frank.ulp.audit.entity.Actor} for failure-path audit events (e.g. {@code
     * MFA_VERIFY_FAILURE}, {@code MFA_LOCKED_OUT}) where {@code SecurityContextHolder} is
     * still empty because the second factor never committed.
     *
     * <p>Returns {@link Optional#empty()} when the entry has expired or the principal does
     * not carry the {@link UserDetails} shape we need for actor attribution.
     */
    public Optional<Authentication> peekPendingAuthentication(String challengeId) {
        if (challengeId == null || challengeId.isBlank()) {
            return Optional.empty();
        }
        return pendingStore.peek(challengeId).map(MfaPendingEntry::getAuthentication)
            .filter(Objects::nonNull).filter(auth -> auth.getPrincipal() instanceof UserDetails);
    }

    /**
     * Read the {@code Retry-After} seconds for a pending challenge that just tripped
     * lockout. The pending entry is peeked (not consumed) so the regular verify pipeline
     * stays the source of truth for state transitions. Returns {@link Optional#empty()}
     * when the pending entry has vanished (e.g. TTL race) — callers should fall back to
     * {@link MfaLockoutService#fallbackWindowSeconds()}.
     */
    public Optional<Long> computeRetryAfterSeconds(String challengeId) {
        if (challengeId == null || challengeId.isBlank()) {
            return Optional.empty();
        }
        return pendingStore.peek(challengeId).flatMap(entry -> {
            Authentication auth = entry.getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof UserDetails ud)) {
                return Optional.empty();
            }
            UserType ut = ud.getUserType();
            if (ut == null || ut.getType() == null || ud.getId() == null) {
                return Optional.empty();
            }
            long remaining = lockoutService.remainingSeconds(ut.getType(), ud.getId());
            return remaining > 0 ? Optional.of(remaining) : Optional.empty();
        });
    }

    private void commitAuthentication(Authentication authentication, HttpServletRequest request,
                                      HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    /**
     * IPv4 same-/24 check: strip the trailing octet on both sides and compare the prefixes
     * as strings. IPv6 addresses bypass the check (returns {@code true}) — the realistic
     * threat model for MFA replay is residential / cellular IPv4 NATs, not IPv6, and a
     * naive byte-prefix on a v6 address would either over-block legitimate users on
     * /64-rotating ISP allocations or be effectively useless. Revisit if production
     * telemetry shows abuse from IPv6 ranges.
     */
    static boolean sameIpv4Subnet(String storedIp, String currentIp) {
        if (storedIp == null || currentIp == null) {
            return false;
        }
        if (storedIp.equals(currentIp)) {
            return true;
        }
        if (storedIp.contains(":") || currentIp.contains(":")) {
            return true;
        }
        return prefix24(storedIp).equals(prefix24(currentIp));
    }

    private static String prefix24(String ipv4) {
        int lastDot = ipv4.lastIndexOf('.');
        return lastDot < 0 ? ipv4 : ipv4.substring(0, lastDot);
    }
}
