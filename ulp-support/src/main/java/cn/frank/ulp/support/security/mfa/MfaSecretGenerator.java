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

import org.springframework.stereotype.Component;

import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;

/**
 * Generates fresh TOTP shared secrets.
 *
 * <p>Uses a 160-bit (20-byte) random secret encoded as Base32, which is the size
 * RFC 4226 §4 R6 recommends as a baseline for HOTP/TOTP — wider than Google
 * Authenticator's 80-bit default and within the limit major authenticator apps
 * accept without truncation.
 *
 * <p>The wrapped {@link DefaultSecretGenerator} draws from {@code SecureRandom}
 * internally, so callers do not need to manage entropy sources.
 */
@Component
public class MfaSecretGenerator {

    private static final int      SECRET_BYTES = 20;
    private final SecretGenerator generator    = new DefaultSecretGenerator(SECRET_BYTES);

    /**
     * @return a Base32-encoded TOTP secret (typically 32 characters for 160 bits)
     *         suitable for {@link MfaOtpAuthUriBuilder} and {@link MfaCodeVerifier}
     */
    public String generate() {
        return generator.generate();
    }
}
