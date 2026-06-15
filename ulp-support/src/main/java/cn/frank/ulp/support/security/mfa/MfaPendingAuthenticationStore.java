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

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.Authentication;

/**
 * Redis-backed pending-challenge store: between primary password auth and the second-factor
 * TOTP verification, the {@link Authentication} object is parked here under a one-shot UUID
 * key {@code ULP_MFA_PENDING:{uuid}} with a 5-minute TTL.
 *
 * <p>Serialization reuses the same {@code RedisSerializer<Object>} bean that Spring Session
 * uses ({@code springSessionDefaultRedisSerializer}) — Jackson 3 + {@code
 * AuthenticationJacksonModule}. This keeps the {@link Authentication} round-trip identical
 * to what Spring Session already does for live sessions, so there's no second JSON schema
 * to maintain.
 *
 * <p>Concurrency: each {@link #stash} mints a fresh UUID, so there's no key collision and
 * no read-modify-write hazard. {@link #consume} uses {@code GETDEL} semantics (atomic
 * fetch+delete) so a successful challenge cannot be replayed.
 */
public class MfaPendingAuthenticationStore {

    private static final String                 KEY_PREFIX  = "ULP_MFA_PENDING:";
    private static final Duration               DEFAULT_TTL = Duration.ofMinutes(5);

    private final RedisTemplate<String, Object> template;
    private final Duration                      ttl;

    public MfaPendingAuthenticationStore(RedisConnectionFactory connectionFactory,
                                         RedisSerializer<Object> valueSerializer) {
        this(connectionFactory, valueSerializer, DEFAULT_TTL);
    }

    public MfaPendingAuthenticationStore(RedisConnectionFactory connectionFactory,
                                         RedisSerializer<Object> valueSerializer, Duration ttl) {
        Objects.requireNonNull(connectionFactory, "connectionFactory");
        Objects.requireNonNull(valueSerializer, "valueSerializer");
        Objects.requireNonNull(ttl, "ttl");
        RedisTemplate<String, Object> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(connectionFactory);
        tpl.setKeySerializer(StringRedisSerializer.UTF_8);
        tpl.setValueSerializer(valueSerializer);
        tpl.setHashKeySerializer(StringRedisSerializer.UTF_8);
        tpl.setHashValueSerializer(valueSerializer);
        tpl.afterPropertiesSet();
        this.template = tpl;
        this.ttl = ttl;
    }

    /**
     * Stash a freshly-authenticated subject and return the challenge UUID the client must
     * present at {@code POST /api/v1/mfa/challenge}.
     */
    public String stash(Authentication authentication, String sourceIp) {
        Objects.requireNonNull(authentication, "authentication");
        String challengeId = UUID.randomUUID().toString();
        MfaPendingEntry entry = new MfaPendingEntry(authentication, sourceIp, Instant.now());
        template.opsForValue().set(key(challengeId), entry, ttl);
        return challengeId;
    }

    /**
     * Peek at the pending entry without consuming it. Returns {@link Optional#empty()} if
     * the entry has expired or never existed. Callers MUST NOT use this for the
     * "successful challenge → commit Authentication" path — that path requires the atomic
     * {@link #consume} so a TOTP submission cannot be replayed.
     */
    public Optional<MfaPendingEntry> peek(String challengeId) {
        if (challengeId == null || challengeId.isBlank()) {
            return Optional.empty();
        }
        Object raw = template.opsForValue().get(key(challengeId));
        return raw instanceof MfaPendingEntry entry ? Optional.of(entry) : Optional.empty();
    }

    /**
     * Atomically fetch the pending entry and delete it from Redis. Returns
     * {@link Optional#empty()} when the key does not exist (already consumed or expired).
     *
     * <p>The fetch+delete is single-round-trip via Redis {@code GETDEL} (Redis 6.2+; the
     * runtime baseline requires Redis 7+, so this is always available).
     */
    public Optional<MfaPendingEntry> consume(String challengeId) {
        if (challengeId == null || challengeId.isBlank()) {
            return Optional.empty();
        }
        Object raw = template.opsForValue().getAndDelete(key(challengeId));
        return raw instanceof MfaPendingEntry entry ? Optional.of(entry) : Optional.empty();
    }

    /**
     * Drop a pending entry without consuming its payload — used when the security layer
     * decides to abort the challenge (e.g. lockout, IP mismatch detected via {@link #peek}
     * before {@link #consume}).
     */
    public void delete(String challengeId) {
        if (challengeId == null || challengeId.isBlank()) {
            return;
        }
        template.delete(key(challengeId));
    }

    private String key(String challengeId) {
        return KEY_PREFIX + challengeId;
    }
}
