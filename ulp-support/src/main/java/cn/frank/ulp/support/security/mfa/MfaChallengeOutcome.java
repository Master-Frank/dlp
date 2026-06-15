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
 * Single-axis outcome of {@link MfaChallengeService#verifyAndCommit}. Each value carries a
 * {@code reason} string aligned with the {@code failure_reason} taxonomy in
 * {@code spec.md} (Phase 6 audit details), so controllers / audit code can use the same
 * code without remapping.
 *
 * <p>{@link #LOCKED_OUT} is a special success-of-detection: the caller MUST NOT consume the
 * pending Redis entry, MUST return HTTP {@code 423} with a {@code Retry-After} header, and
 * MUST audit it as a separate {@code MFA_LOCKED_OUT} event (Phase 6).
 */
public enum MfaChallengeOutcome {

                                 /** Verification succeeded; {@link MfaChallengeService} has already committed the
                                  *  {@link org.springframework.security.core.Authentication} into the session. */
                                 SUCCESS(null),

                                 /** Lockout threshold tripped — caller emits 423 + {@code Retry-After}. */
                                 LOCKED_OUT("locked_out"),

                                 /** No pending entry for the supplied challenge id (TTL expired or never existed). */
                                 CHALLENGE_EXPIRED("challenge_expired"),

                                 /** Pending entry exists but the source IP no longer matches its /24 subnet, or the
                                  *  principal type does not map to any {@link MfaService} bean. */
                                 CHALLENGE_SESSION_INVALID("challenge_session_invalid"),

                                 /** TOTP code did not verify. */
                                 INVALID_OTP("invalid_otp"),

                                 /** Backup code did not match any unused entry — Phase 5. */
                                 INVALID_BACKUP_CODE("invalid_backup_code"),

                                 /** Subject lost its MFA binding (cipher cleared) between primary auth and challenge —
                                  *  treat as session-invalid: the user must re-authenticate. */
                                 SUBJECT_NOT_BOUND("challenge_session_invalid");

    private final String reason;

    MfaChallengeOutcome(String reason) {
        this.reason = reason;
    }

    /** Audit / response failure_reason code; {@code null} for {@link #SUCCESS}. */
    public String reason() {
        return reason;
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
