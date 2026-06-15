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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds {@code ulp.mfa.*} configuration. {@code keyEncryptionKey} is the AES-256
 * Key Encryption Key used to wrap TOTP shared secrets at rest; the deployable's
 * {@code application.yml} bridges {@code ULP_MFA_KEK} env to this property.
 *
 * <p>Validation is performed by {@link MfaSecretCipher} at construction; the
 * application MUST fail to start when the KEK is missing or malformed.
 */
@ConfigurationProperties(prefix = "ulp.mfa")
public class MfaProperties {

    /** Base64-encoded 32-byte (256-bit) Key Encryption Key. */
    private String keyEncryptionKey;

    public String getKeyEncryptionKey() {
        return keyEncryptionKey;
    }

    public void setKeyEncryptionKey(String keyEncryptionKey) {
        this.keyEncryptionKey = keyEncryptionKey;
    }
}
