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

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Micrometer wrapper for the MFA subsystem — three Counters + one Gauge, registered on
 * construction so they appear in {@code /actuator/prometheus} the moment the bean is wired.
 *
 * <p>Counters (always-on, tag-cardinality kept low so Prometheus storage stays cheap):
 * <ul>
 *   <li>{@code ulp_mfa_verify_total{subject_type, via, outcome}} — every second-factor
 *       verification attempt routed through {@link MfaChallengeService}. {@code via} ∈
 *       {@code {totp, backup}}; {@code outcome} ∈ {@code {success, invalid_otp,
 *       invalid_backup_code, challenge_expired, challenge_session_invalid, subject_not_bound,
 *       locked_out}}.</li>
 *   <li>{@code ulp_mfa_lockout_total{subject_type, phase}} — incremented every time a
 *       request is rejected with a {@code LOCKED_OUT} outcome (the lock just tripped or the
 *       subject was already in cooldown). At {@code bind_confirm}/{@code unbind} the
 *       controller can distinguish the fresh trip from a rejection-while-locked; at
 *       {@code challenge} the service layer doesn't surface that distinction so the counter
 *       collapses both. Either way, {@code rate(...)} answers the operational question
 *       ("how fast are we rejecting users right now"). {@code phase} ∈ {@code {challenge,
 *       bind_confirm, unbind}}.</li>
 *   <li>{@code ulp_mfa_bind_total{subject_type, phase, outcome}} — bind / unbind lifecycle
 *       counter. {@code phase} ∈ {@code {prepare, confirm, unbind}}; {@code outcome} ∈
 *       {@code {success, invalid_otp, locked_out, blocked_by_org_policy, error}}.</li>
 * </ul>
 *
 * <p>Gauge: {@code ulp_mfa_pending_active} — scans Redis for keys matching
 * {@code ULP_MFA_PENDING:*} and returns the count of in-flight challenges. Scrape-time scans
 * would hammer Redis under heavy load, so the value is cached for at least
 * {@link #DEFAULT_SCAN_INTERVAL_MS} (30 s by default — matches spec.md tasks 6.8 "≥30s
 * 采样"). The cached value is returned for intermediate scrapes; on the next scrape past the
 * interval the supplier re-scans, races between scrapes resolve via double-checked locking.
 *
 * <p>Failure modes: if Redis is unavailable during a refresh, the previous cached value is
 * served and a warning is logged. Counters use Micrometer's built-in {@code Counter.builder}
 * + {@code register} so duplicate registrations across tests are idempotent.
 *
 * <p>Wiring contract: declared once as {@code @Bean} per deployable (console + portal). Both
 * {@link MfaChallengeService} and the controller tier accept it as optional injection — when
 * absent the counters/gauge silently no-op so tests that don't care about metrics keep
 * working without changes.
 */
public class MfaMetrics {

    private static final Logger       log                      = LoggerFactory
        .getLogger(MfaMetrics.class);

    private static final String       PENDING_KEY_PREFIX       = "ULP_MFA_PENDING:";
    private static final long         DEFAULT_SCAN_INTERVAL_MS = 30_000L;
    private static final long         DEFAULT_SCAN_BATCH_COUNT = 500L;

    private final MeterRegistry       registry;
    private final StringRedisTemplate redisTemplate;
    private final long                scanIntervalMs;
    private final AtomicLong          cachedPendingCount       = new AtomicLong(0L);
    private volatile long             lastScanMillis           = 0L;
    private final Object              scanLock                 = new Object();

    public MfaMetrics(MeterRegistry registry, StringRedisTemplate redisTemplate) {
        this(registry, redisTemplate, DEFAULT_SCAN_INTERVAL_MS);
    }

    public MfaMetrics(MeterRegistry registry, StringRedisTemplate redisTemplate,
                      long scanIntervalMs) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.redisTemplate = Objects.requireNonNull(redisTemplate, "redisTemplate");
        if (scanIntervalMs < 1_000L) {
            throw new IllegalArgumentException("scanIntervalMs must be >= 1000");
        }
        this.scanIntervalMs = scanIntervalMs;
        Gauge.builder("ulp_mfa_pending_active", this, MfaMetrics::currentPendingCount)
            .description("In-flight MFA challenges parked in Redis (cached SCAN)")
            .strongReference(true).register(registry);
    }

    /**
     * Record one second-factor verification outcome. {@code subjectType} should align with
     * {@link MfaService#subjectType()} (currently {@code "user"} / {@code "admin"});
     * {@code via} is {@code "totp"} or {@code "backup"}; {@code outcome} is the
     * lower-cased {@link MfaChallengeOutcome} name. Use {@link #verifyOutcome(String, String,
     * MfaChallengeOutcome)} for the typed overload.
     */
    public void verifyOutcome(String subjectType, String via, MfaChallengeOutcome outcome) {
        if (outcome == null) {
            return;
        }
        verifyOutcome(subjectType, via, outcome.name().toLowerCase(Locale.ROOT));
    }

    public void verifyOutcome(String subjectType, String via, String outcome) {
        Counter.builder("ulp_mfa_verify_total")
            .description("MFA second-factor verification attempts by outcome")
            .tag("subject_type", nullSafe(subjectType)).tag("via", nullSafe(via))
            .tag("outcome", nullSafe(outcome)).register(registry).increment();
    }

    /**
     * Record a lockout trip — i.e. the failure counter just crossed the threshold, not every
     * failed attempt while already locked. {@code phase} distinguishes the entry point:
     * {@code challenge} for {@link MfaChallengeService}, {@code bind_confirm} / {@code unbind}
     * for the self-service controllers.
     */
    public void lockout(String subjectType, String phase) {
        Counter.builder("ulp_mfa_lockout_total")
            .description("MFA failure counter trips into locked state")
            .tag("subject_type", nullSafe(subjectType)).tag("phase", nullSafe(phase))
            .register(registry).increment();
    }

    /**
     * Record one bind-lifecycle event. {@code phase} ∈ {@code {prepare, confirm, unbind}};
     * {@code outcome} ∈ {@code {success, invalid_otp, locked_out, blocked_by_org_policy,
     * error}}. The combination keeps tag cardinality bounded at 2 × 3 × 5 = 30 series per
     * deployable.
     */
    public void bind(String subjectType, String phase, String outcome) {
        Counter.builder("ulp_mfa_bind_total").description("MFA bind / unbind lifecycle events")
            .tag("subject_type", nullSafe(subjectType)).tag("phase", nullSafe(phase))
            .tag("outcome", nullSafe(outcome)).register(registry).increment();
    }

    private double currentPendingCount() {
        long now = System.currentTimeMillis();
        if (now - lastScanMillis < scanIntervalMs) {
            return cachedPendingCount.get();
        }
        synchronized (scanLock) {
            if (now - lastScanMillis < scanIntervalMs) {
                return cachedPendingCount.get();
            }
            long count = 0L;
            try (Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(PENDING_KEY_PREFIX + "*").count(DEFAULT_SCAN_BATCH_COUNT).build())) {
                while (cursor.hasNext()) {
                    cursor.next();
                    count++;
                }
            } catch (RuntimeException ex) {
                // Redis transient failure — serve the previous snapshot and try again next
                // scrape past the interval. Don't throw: a scrape failure here would taint
                // every unrelated metric on the same /actuator/prometheus endpoint.
                log.warn("ulp_mfa_pending_active SCAN failed, serving cached value", ex);
                lastScanMillis = now;
                return cachedPendingCount.get();
            }
            cachedPendingCount.set(count);
            lastScanMillis = now;
            return count;
        }
    }

    MeterRegistry registry() {
        return registry;
    }

    private static String nullSafe(String tag) {
        return tag == null || tag.isBlank() ? "unknown" : tag;
    }
}
