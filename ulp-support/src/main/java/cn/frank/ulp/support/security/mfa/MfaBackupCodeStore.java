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
 * Narrow DAO contract for the {@code backup_codes_json} column on the per-subject table.
 *
 * <p>{@link MfaBackupCodeService} routes by {@link #subjectType()} (the same {@code "admin"} /
 * {@code "user"} key used by {@link MfaService}) so each deployable contributes one
 * {@code @Component} bean and the consume logic stays in ulp-support.
 *
 * <p>Implementations MUST do the save in a transaction — the consume cycle is
 * load → match → remove → save and the gap between read and write is the consume-once
 * boundary. A simple per-row optimistic save inside {@code @Transactional} is enough for
 * the current usage (a user can hit at most a handful of concurrent challenge attempts);
 * if real contention shows up later, swap in {@code SELECT ... FOR UPDATE} or a Redis
 * lock around {@link MfaBackupCodeService#consume(String, String, String)}.
 */
public interface MfaBackupCodeStore {

    /** {@code "admin"} or {@code "user"} — must match the sibling {@link MfaService#subjectType()}. */
    String subjectType();

    /**
     * @return the raw JSON array stored in the subject's {@code backup_codes_json} column, or
     *         {@code null} when the column is null (subject never bound, or unbound / reset).
     */
    String loadBackupCodesJson(String userId);

    /**
     * Persist the (possibly empty) JSON array back to the subject's row. {@code null} or
     * an empty JSON array {@code "[]"} means "all consumed" — the column SHOULD reflect
     * exactly what was passed so the frontend can detect the "regenerate required" state.
     */
    void saveBackupCodesJson(String userId, String backupCodesJson);
}
