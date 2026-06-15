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

import java.util.List;

/**
 * Result of {@link MfaService#confirmBind(String, String)}.
 *
 * <p>{@code backupCodes} contains the 10 single-use codes in plaintext — this is the only
 * API response that ever carries them. The hashed forms are persisted; no re-fetch endpoint
 * exists, by design.
 */
public record MfaConfirmBindResult(List<String> backupCodes) {
}
