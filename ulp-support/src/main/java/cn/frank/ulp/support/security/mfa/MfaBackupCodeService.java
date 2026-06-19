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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;

/**
 * Phase-5 backup-code consume service.
 *
 * <p>{@link AbstractMfaService#confirmBind(String, String)} writes a JSON array of
 * encoded backup codes (each encoded via the shared {@link PasswordEncoder} — currently
 * Argon2id) into the subject's {@code backup_codes_json} column. This service is the
 * only path that:
 * <ol>
 *   <li>matches a plaintext code submitted at challenge time against the stored hashes
 *       via {@link PasswordEncoder#matches(CharSequence, String)};</li>
 *   <li>removes the matched entry from the JSON list — single-use semantics;</li>
 *   <li>writes the trimmed list back via the per-subject {@link MfaBackupCodeStore}.</li>
 * </ol>
 *
 * <p>Routing follows the same pattern as {@link MfaChallengeService}: each deployable
 * contributes a {@link MfaBackupCodeStore} bean keyed by {@link MfaService#subjectType()}
 * (admin in ulp-console, user in ulp-portal), and this service picks the right store via
 * the constructor-time {@code Map}.
 *
 * <p>The matched entry is removed BEFORE the save, so a transient DB error rolls back via
 * the store's {@code @Transactional} guard and leaves the original JSON in place — the
 * user is told "invalid" and can retry. A successful save without a match is impossible
 * (we only save when we found a match). Concurrent same-user requests racing for the
 * last code: both load the same JSON, both match, both remove — the {@code @Transactional}
 * save in the store class is the last-writer-wins boundary. Acceptable for the threat
 * model (consume-twice would only let the same user reuse their own one-time code) and
 * the lockout counter still gates brute force.
 */
public class MfaBackupCodeService {

    private final Map<String, MfaBackupCodeStore> storesByType;
    private final PasswordEncoder                 passwordEncoder;

    public MfaBackupCodeService(Collection<MfaBackupCodeStore> stores,
                                PasswordEncoder passwordEncoder) {
        Objects.requireNonNull(stores, "stores");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder");
        this.storesByType = stores.stream().collect(
            Collectors.toUnmodifiableMap(MfaBackupCodeStore::subjectType, Function.identity()));
    }

    /**
     * Attempt to consume one backup code for the given subject.
     *
     * <p>Outcome semantics:
     * <ul>
     *   <li>no store registered for {@code userType} → returns
     *       {@code (consumed=false, matched=false, remaining=0)} — the caller should treat
     *       this as a configuration error and surface {@code challenge_session_invalid}.</li>
     *   <li>subject never bound MFA (column is null) → returns
     *       {@code (consumed=false, matched=false, remaining=0)}.</li>
     *   <li>code does not match any stored hash → returns
     *       {@code (consumed=false, matched=false, remaining=<unchanged>)}.</li>
     *   <li>code matches a stored hash → removes it, saves the trimmed list, returns
     *       {@code (consumed=true, matched=true, remaining=<post-remove>)}.</li>
     * </ul>
     */
    public BackupCodeConsumption consume(String userType, String userId, String code) {
        Objects.requireNonNull(userType, "userType");
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(code, "code");

        MfaBackupCodeStore store = storesByType.get(userType);
        if (store == null) {
            return BackupCodeConsumption.miss(0);
        }

        String raw = store.loadBackupCodesJson(userId);
        List<String> encoded = parse(raw);
        if (encoded.isEmpty()) {
            return BackupCodeConsumption.miss(0);
        }

        int matchedIndex = -1;
        for (int i = 0; i < encoded.size(); i++) {
            if (passwordEncoder.matches(code, encoded.get(i))) {
                matchedIndex = i;
                break;
            }
        }
        if (matchedIndex < 0) {
            return BackupCodeConsumption.miss(encoded.size());
        }

        List<String> trimmed = new ArrayList<>(encoded);
        trimmed.remove(matchedIndex);
        store.saveBackupCodesJson(userId, JSON.toJSONString(trimmed));
        return BackupCodeConsumption.hit(trimmed.size());
    }

    /**
     * Peek at the remaining count without consuming. Used by the bind confirm response to
     * report initial count, and by the frontend to refresh the regenerate-warning state
     * without re-spending a code. Returns {@code 0} when the subject is not bound or the
     * route is unknown.
     */
    public int remaining(String userType, String userId) {
        Objects.requireNonNull(userType, "userType");
        Objects.requireNonNull(userId, "userId");
        MfaBackupCodeStore store = storesByType.get(userType);
        if (store == null) {
            return 0;
        }
        return parse(store.loadBackupCodesJson(userId)).size();
    }

    private static List<String> parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<String> list = JSON.parseArray(raw, String.class);
            return list == null ? Collections.emptyList() : list;
        } catch (JSONException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Single-shot outcome of {@link #consume(String, String, String)}. {@code consumed}
     * means we actually wrote the trimmed list back — i.e. a code matched and was spent.
     * {@code remaining} is the post-save count; for a miss it is the pre-call count
     * (unchanged), and for a hit it is one less than the pre-call count.
     */
    public record BackupCodeConsumption(boolean consumed, int remaining) {
        public static BackupCodeConsumption hit(int remaining) {
            return new BackupCodeConsumption(true, remaining);
        }

        public static BackupCodeConsumption miss(int remaining) {
            return new BackupCodeConsumption(false, remaining);
        }
    }
}
