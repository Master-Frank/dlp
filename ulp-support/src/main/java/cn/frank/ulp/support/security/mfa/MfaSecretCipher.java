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

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * AES-256-GCM wrap/unwrap for TOTP shared secrets at rest.
 *
 * <p>Ciphertext layout (Base64-encoded): {@code nonce(12 bytes) || ciphertext || authTag(16 bytes)}.
 * The 128-bit auth tag is included as the trailing portion of the GCM ciphertext per JCA convention.
 *
 * <p>KEK lifecycle: read once from {@link MfaProperties#getKeyEncryptionKey()} at startup,
 * validated by {@link #validateKek()} ({@code @PostConstruct}). The application MUST fail to start
 * when the KEK is missing, not valid Base64, or not exactly 32 bytes decoded — a silently weak
 * KEK would void the at-rest protection promised by {@code security-baseline}.
 */
@Component
public class MfaSecretCipher {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ALGORITHM      = "AES";
    private static final int    NONCE_LENGTH   = 12;
    private static final int    TAG_BITS       = 128;
    private static final int    KEK_BYTES      = 32;

    private final MfaProperties properties;
    private final SecureRandom  secureRandom   = new SecureRandom();
    private SecretKeySpec       kek;

    public MfaSecretCipher(MfaProperties properties) {
        this.properties = Objects.requireNonNull(properties, "properties");
    }

    @PostConstruct
    void validateKek() {
        String configured = properties.getKeyEncryptionKey();
        if (configured == null || configured.isBlank()) {
            throw new IllegalStateException(
                "ulp.mfa.key-encryption-key is missing. Generate a KEK with "
                                            + "`openssl rand -base64 32` and set ULP_MFA_KEK env or "
                                            + "ulp.mfa.key-encryption-key property.");
        }
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(configured.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("ulp.mfa.key-encryption-key is not valid Base64", e);
        }
        if (decoded.length != KEK_BYTES) {
            throw new IllegalStateException("ulp.mfa.key-encryption-key must decode to exactly "
                                            + KEK_BYTES + " bytes (got " + decoded.length + ")");
        }
        this.kek = new SecretKeySpec(decoded, ALGORITHM);
    }

    /**
     * Encrypt a TOTP shared secret. Each call generates a fresh nonce, so repeated
     * encryption of the same plaintext yields different ciphertexts (semantic security).
     *
     * @param plaintext the TOTP secret bytes (typically a 20-byte raw value)
     * @return Base64-encoded {@code nonce || ciphertext || authTag}
     */
    public String encrypt(byte[] plaintext) {
        Objects.requireNonNull(plaintext, "plaintext");
        byte[] nonce = new byte[NONCE_LENGTH];
        secureRandom.nextBytes(nonce);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, kek, new GCMParameterSpec(TAG_BITS, nonce));
            byte[] ciphertext = cipher.doFinal(plaintext);
            byte[] out = ByteBuffer.allocate(nonce.length + ciphertext.length).put(nonce)
                .put(ciphertext).array();
            return Base64.getEncoder().encodeToString(out);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("AES-GCM encryption failed", e);
        }
    }

    /**
     * Decrypt a previously {@link #encrypt(byte[]) encrypted} TOTP secret. Throws
     * {@link IllegalStateException} on any tampering — the 128-bit GCM tag MUST verify.
     */
    public byte[] decrypt(String cipherTextBase64) {
        Objects.requireNonNull(cipherTextBase64, "cipherTextBase64");
        byte[] all;
        try {
            all = Base64.getDecoder().decode(cipherTextBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("ciphertext is not valid Base64", e);
        }
        if (all.length <= NONCE_LENGTH) {
            throw new IllegalStateException("ciphertext too short");
        }
        byte[] nonce = new byte[NONCE_LENGTH];
        byte[] ct = new byte[all.length - NONCE_LENGTH];
        System.arraycopy(all, 0, nonce, 0, NONCE_LENGTH);
        System.arraycopy(all, NONCE_LENGTH, ct, 0, ct.length);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, kek, new GCMParameterSpec(TAG_BITS, nonce));
            return cipher.doFinal(ct);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("AES-GCM decryption failed", e);
        }
    }
}
