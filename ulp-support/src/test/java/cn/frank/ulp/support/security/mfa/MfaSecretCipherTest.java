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
import java.security.SecureRandom;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Covers the three startup-fail paths (missing / non-Base64 / wrong-length KEK),
 * the round-trip invariant, and the nonce-uniqueness guarantee that gives the
 * cipher its semantic security.
 */
class MfaSecretCipherTest {

    private static String validKekBase64() {
        byte[] raw = new byte[32];
        new SecureRandom().nextBytes(raw);
        return Base64.getEncoder().encodeToString(raw);
    }

    private MfaSecretCipher cipher;

    @BeforeEach
    void setUp() {
        MfaProperties props = new MfaProperties();
        props.setKeyEncryptionKey(validKekBase64());
        cipher = new MfaSecretCipher(props);
        cipher.validateKek();
    }

    @Test
    void encryptThenDecrypt_returnsOriginalPlaintext() {
        byte[] plaintext = "JBSWY3DPEHPK3PXP".getBytes(StandardCharsets.UTF_8);
        String token = cipher.encrypt(plaintext);
        byte[] decoded = cipher.decrypt(token);
        assertThat(decoded).isEqualTo(plaintext);
    }

    @Test
    void encrypt_producesDistinctCiphertextsForSamePlaintext() {
        byte[] plaintext = "same-secret".getBytes(StandardCharsets.UTF_8);
        String a = cipher.encrypt(plaintext);
        String b = cipher.encrypt(plaintext);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void decrypt_throwsOnTamperedCiphertext() {
        byte[] plaintext = "tamper-me".getBytes(StandardCharsets.UTF_8);
        String token = cipher.encrypt(plaintext);
        byte[] raw = Base64.getDecoder().decode(token);
        raw[raw.length - 1] ^= 0x01;
        String tampered = Base64.getEncoder().encodeToString(raw);
        assertThatThrownBy(() -> cipher.decrypt(tampered))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void missingKek_failsStartup() {
        MfaProperties props = new MfaProperties();
        props.setKeyEncryptionKey(null);
        MfaSecretCipher c = new MfaSecretCipher(props);
        assertThatThrownBy(c::validateKek).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("ulp.mfa.key-encryption-key is missing");
    }

    @Test
    void blankKek_failsStartup() {
        MfaProperties props = new MfaProperties();
        props.setKeyEncryptionKey("   ");
        MfaSecretCipher c = new MfaSecretCipher(props);
        assertThatThrownBy(c::validateKek).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("missing");
    }

    @Test
    void invalidBase64Kek_failsStartup() {
        MfaProperties props = new MfaProperties();
        props.setKeyEncryptionKey("!!! not base64 !!!");
        MfaSecretCipher c = new MfaSecretCipher(props);
        assertThatThrownBy(c::validateKek).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("not valid Base64");
    }

    @Test
    void wrongLengthKek_failsStartup() {
        byte[] shortKey = new byte[16];
        new SecureRandom().nextBytes(shortKey);
        MfaProperties props = new MfaProperties();
        props.setKeyEncryptionKey(Base64.getEncoder().encodeToString(shortKey));
        MfaSecretCipher c = new MfaSecretCipher(props);
        assertThatThrownBy(c::validateKek).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("must decode to exactly 32 bytes");
    }
}
