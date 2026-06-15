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

import java.util.Objects;

import org.springframework.stereotype.Component;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;

/**
 * RFC 6238 TOTP code verification wrapper.
 *
 * <p>Algorithm parameters MUST match {@link MfaOtpAuthUriBuilder}: HmacSHA1, 30-second
 * period, 6-digit code. The ±1 discrepancy window allows a single past/future period
 * to tolerate ~30 s of clock skew between the user's authenticator and the server —
 * standard practice for authenticator apps. Wider windows materially weaken brute-force
 * resistance and are intentionally not exposed.
 *
 * <p>{@link DefaultCodeVerifier#isValidCode(String, String)} performs constant-time
 * comparison internally via {@code MessageDigest.isEqual}, so timing-side-channel
 * leaks of the correct code are mitigated.
 */
@Component
public class MfaCodeVerifier {

    private static final int   TIME_PERIOD_SECONDS = 30;
    private static final int   ALLOWED_DISCREPANCY = 1;
    private final CodeVerifier verifier;

    public MfaCodeVerifier() {
        DefaultCodeVerifier impl = new DefaultCodeVerifier(
            new DefaultCodeGenerator(HashingAlgorithm.SHA1), new SystemTimeProvider());
        impl.setTimePeriod(TIME_PERIOD_SECONDS);
        impl.setAllowedTimePeriodDiscrepancy(ALLOWED_DISCREPANCY);
        this.verifier = impl;
    }

    /**
     * @param secretBase32 the user's TOTP shared secret in Base32 (as issued by
     *                     {@link MfaSecretGenerator})
     * @param code         the 6-digit code submitted by the user
     * @return true iff {@code code} matches the current period or one period earlier / later
     */
    public boolean isValid(String secretBase32, String code) {
        Objects.requireNonNull(secretBase32, "secretBase32");
        Objects.requireNonNull(code, "code");
        return verifier.isValidCode(secretBase32, code);
    }
}
