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

import org.junit.jupiter.api.Test;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the ±1 time-window contract. We compute codes against three nominal
 * periods (-2, -1, 0, +1, +2 × 30 s) using a separate {@link DefaultCodeGenerator}
 * instance — the same primitive the production verifier consumes — to ensure the
 * algorithm parameters (HmacSHA1, 30 s, 6 digits) line up across both sides.
 *
 * <p>{@link MfaCodeVerifier#isValid(String, String)} ultimately delegates to
 * {@code DefaultCodeVerifier}, which uses {@code MessageDigest.isEqual} for the
 * constant-time string compare; this is a JDK guarantee, not retested here.
 */
class MfaCodeVerifierTest {

    private static final int           PERIOD_SECONDS = 30;
    /** RFC 6238 §B.1 reference secret encoded as Base32 ("12345678901234567890"). */
    private static final String        SECRET_BASE32  = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ";

    private final MfaCodeVerifier      verifier       = new MfaCodeVerifier();
    private final DefaultCodeGenerator generator      = new DefaultCodeGenerator(
        HashingAlgorithm.SHA1);

    private String codeForPeriodOffset(int offsetPeriods) throws Exception {
        long nowEpoch = System.currentTimeMillis() / 1000L;
        long counter = nowEpoch / PERIOD_SECONDS + offsetPeriods;
        return generator.generate(SECRET_BASE32, counter);
    }

    @Test
    void currentPeriodCode_isValid() throws Exception {
        assertThat(verifier.isValid(SECRET_BASE32, codeForPeriodOffset(0))).isTrue();
    }

    @Test
    void previousPeriodCode_isValid_withinDiscrepancy() throws Exception {
        assertThat(verifier.isValid(SECRET_BASE32, codeForPeriodOffset(-1))).isTrue();
    }

    @Test
    void nextPeriodCode_isValid_withinDiscrepancy() throws Exception {
        assertThat(verifier.isValid(SECRET_BASE32, codeForPeriodOffset(1))).isTrue();
    }

    @Test
    void twoPeriodsAgo_isRejected() throws Exception {
        assertThat(verifier.isValid(SECRET_BASE32, codeForPeriodOffset(-2))).isFalse();
    }

    @Test
    void twoPeriodsAhead_isRejected() throws Exception {
        assertThat(verifier.isValid(SECRET_BASE32, codeForPeriodOffset(2))).isFalse();
    }

    @Test
    void garbageCode_isRejected() {
        assertThat(verifier.isValid(SECRET_BASE32, "000000")).isFalse();
        assertThat(verifier.isValid(SECRET_BASE32, "abcdef")).isFalse();
    }
}
