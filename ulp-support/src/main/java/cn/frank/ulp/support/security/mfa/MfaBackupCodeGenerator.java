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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * One-time MFA backup codes generator.
 *
 * <p>Generates 10 codes of 8 characters each from the alphabet
 * {@code 2-9 A-H J-N P-Z} (32 symbols). The alphabet intentionally drops the
 * visually-ambiguous {@code 0/O/I/1} characters so a user transcribing from a
 * printed sheet is less likely to mis-type. Codes are unformatted (no dashes);
 * the consumer is responsible for any display formatting.
 *
 * <p>Each code carries {@code log2(32) * 8 = 40 bits} of entropy — enough to
 * resist guessing when paired with a per-account rate limit, but a forced
 * regeneration MUST happen once a user has consumed all 10, as documented in
 * {@code security-baseline}.
 *
 * <p>Codes are hashed with Argon2id at rest (handled by the storage layer, not
 * here) — this class only emits plaintext for the bind-confirm response.
 */
@Component
public class MfaBackupCodeGenerator {

    private static final char[] ALPHABET     = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
    private static final int    CODE_LENGTH  = 8;
    private static final int    CODE_COUNT   = 10;

    private final SecureRandom  secureRandom = new SecureRandom();

    /**
     * @return exactly {@value #CODE_COUNT} fresh codes; the returned list is mutable
     *         and owned by the caller
     */
    public List<String> generate() {
        List<String> codes = new ArrayList<>(CODE_COUNT);
        for (int i = 0; i < CODE_COUNT; i++) {
            codes.add(generateOne());
        }
        return codes;
    }

    private String generateOne() {
        char[] buf = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            buf[i] = ALPHABET[secureRandom.nextInt(ALPHABET.length)];
        }
        return new String(buf);
    }
}
