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
 * Result of {@link MfaService#prepareBind(String)}.
 *
 * <p>The {@code otpAuthUri} is what the user's authenticator app consumes (typically by scanning
 * a QR rendered client-side); {@code secretBase32} is the same secret shown as a fallback for
 * manual entry. The plaintext secret leaves the server only on this response — once
 * {@link MfaService#confirmBind(String, String)} succeeds, the secret is wiped from the
 * staging cache and only the ciphertext lives in DB.
 */
public record MfaPrepareBindResult(String otpAuthUri, String secretBase32) {
}
