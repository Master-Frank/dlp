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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.stereotype.Component;

/**
 * Builds {@code otpauth://} URIs per the Google Authenticator
 * <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">Key
 * Uri Format</a>, which all mainstream authenticator apps consume.
 *
 * <p>Output shape:
 * <pre>otpauth://totp/{ISSUER}:{ACCOUNT}?secret=...&amp;issuer={ISSUER}&amp;algorithm=SHA1&amp;digits=6&amp;period=30</pre>
 *
 * <p>Both the path component {@code ISSUER:ACCOUNT} (the "label") and the
 * {@code issuer} query parameter are present — duplication is intentional and
 * recommended by the spec for legacy app compatibility.
 *
 * <p>Algorithm parameters MUST stay in sync with {@link MfaCodeVerifier}
 * (HmacSHA1, 30-second period, 6 digits). Changing one without the other will
 * silently invalidate every existing user binding.
 */
@Component
public class MfaOtpAuthUriBuilder {

    private static final String ISSUER      = "ULP";
    private static final String ALGORITHM   = "SHA1";
    private static final int    DIGITS      = 6;
    private static final int    PERIOD_SECS = 30;

    /**
     * @param account     account label (e.g. username or email) shown in the authenticator app
     * @param secretBase32 the Base32 TOTP secret from {@link MfaSecretGenerator}
     */
    public String build(String account, String secretBase32) {
        Objects.requireNonNull(account, "account");
        Objects.requireNonNull(secretBase32, "secretBase32");
        String encodedAccount = URLEncoder.encode(account, StandardCharsets.UTF_8);
        String encodedIssuer = URLEncoder.encode(ISSUER, StandardCharsets.UTF_8);
        return "otpauth://totp/" + encodedIssuer + ":" + encodedAccount + "?secret=" + secretBase32
               + "&issuer=" + encodedIssuer + "&algorithm=" + ALGORITHM + "&digits=" + DIGITS
               + "&period=" + PERIOD_SECS;
    }
}
