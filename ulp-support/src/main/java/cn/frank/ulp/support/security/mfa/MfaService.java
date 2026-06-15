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

/**
 * Self-service MFA lifecycle for a single subject type (administrator or end-user).
 *
 * <p>Two concrete beans implement this interface — one in {@code ulp-console} operating on
 * {@code ulp_administrator}, one in {@code ulp-portal} operating on {@code ulp_user}. They
 * differ only in DAO + Redis key namespace; the policy (TTL, validation, rehash discipline)
 * lives in the contract.
 *
 * <p>Admin reset (clearing another subject's binding) is intentionally NOT here — it lives in
 * a dedicated {@code AdminMfaResetService} in {@code ulp-console}, gated by ADMIN role.
 */
public interface MfaService {

    /**
     * Generate a new TOTP secret and stage it in Redis under
     * {@code ULP_BIND_MFA_SECRET:{userType}:{userId}} with a 10-minute TTL. The plaintext
     * secret is returned to the caller in the {@link MfaPrepareBindResult#otpAuthUri()}
     * and {@link MfaPrepareBindResult#secretBase32()}; nothing is written to the DB yet.
     *
     * <p>If a previous {@code prepareBind} for the same subject is still pending, it is
     * overwritten — only the most recent prepare survives.
     *
     * @param userId the subject id (administrator id for console, user id for portal)
     * @return the bind challenge payload
     * @throws IllegalStateException if the subject already has {@code mfa_enabled=true}
     */
    MfaPrepareBindResult prepareBind(String userId);

    /**
     * Verify the user's first OTP against the staged secret, then commit:
     *
     * <ol>
     *   <li>encrypt secret via {@link MfaSecretCipher} → write to {@code totp_secret_cipher}
     *   <li>generate 10 backup codes, hash each, write JSON array to {@code backup_codes_json}
     *   <li>set {@code mfa_enabled=true}
     *   <li>delete the staged Redis entry
     * </ol>
     *
     * <p>Returns the plaintext backup codes in the response — the only time they leave the
     * server. No re-fetch endpoint exists; lost codes require a fresh prepare/confirm cycle.
     *
     * @throws IllegalStateException if no prepare is staged or it has expired
     * @throws cn.frank.ulp.support.exception.BadParamsException if the OTP does not verify
     */
    MfaConfirmBindResult confirmBind(String userId, String otp);

    /**
     * Disable MFA for the subject. Requires a fresh current OTP — backup codes are NOT
     * accepted for unbind (RFC 6238 mitigations expect a live possession proof for the
     * destructive op). Clears {@code totp_secret_cipher}, {@code backup_codes_json}, and
     * sets {@code mfa_enabled=false}.
     *
     * <p>Portal callers MUST consult {@code OrgMfaPolicyService.isUserEnforced(userId)}
     * BEFORE invoking this method — if true, return 403 {@code unbind_blocked_by_org_policy}
     * and skip the OTP check entirely (no failure-counter consumption).
     *
     * @throws cn.frank.ulp.support.exception.BadParamsException if the OTP does not verify
     * @throws IllegalStateException if MFA is not currently enabled for the subject
     */
    void unbind(String userId, String currentOtp);

    /**
     * Subject-type discriminator used to route {@link MfaService} beans to the right
     * principal: {@code "admin"} for {@code ulp_administrator}, {@code "user"} for
     * {@code ulp_user}. Must align with the {@code UserType.getType()} value so callers
     * can map {@code Authentication.principal.userType.type → MfaService} via
     * dependency-injected {@link java.util.Collection}.
     */
    String subjectType();

    /**
     * Return the encrypted TOTP secret currently bound to this subject, or {@code null}
     * when the subject has not enabled MFA. Used by the challenge-verification path to
     * decrypt the secret and validate the submitted OTP without going through the bind
     * lifecycle.
     */
    String loadActiveCipher(String userId);
}
