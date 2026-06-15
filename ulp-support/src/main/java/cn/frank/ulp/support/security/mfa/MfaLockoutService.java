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
import java.util.Objects;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Brute-force throttle for MFA verification — Phase 3 wires {@link #recordFailure} and
 * {@link #isLockedOut} into the challenge path; Phase 5 will additionally hook
 * bind/unbind/admin-reset (see tasks.md §5.3).
 *
 * <p>Counter: {@code ULP_MFA_FAIL:{userType}:{userId}} with a sliding TTL of 15 minutes.
 * Each failure {@code INCR}s the counter and re-applies the TTL. At threshold (5) the
 * service starts reporting {@link #isLockedOut} {@code true}; {@link #remainingSeconds}
 * returns the {@code Retry-After} value the caller should emit alongside HTTP 423.
 *
 * <p>Success clears the counter unconditionally — a successful TOTP at attempt 4 must
 * leave the user with a clean slate. The lockout window itself does NOT consume the
 * pending Redis entry; the caller is responsible for short-circuiting before
 * {@link MfaPendingAuthenticationStore#consume} when {@link #isLockedOut} is true.
 */
public class MfaLockoutService {

    private static final String       KEY_PREFIX        = "ULP_MFA_FAIL:";
    private static final int          DEFAULT_THRESHOLD = 5;
    private static final Duration     DEFAULT_WINDOW    = Duration.ofMinutes(15);

    private final StringRedisTemplate redisTemplate;
    private final int                 threshold;
    private final Duration            window;

    public MfaLockoutService(StringRedisTemplate redisTemplate) {
        this(redisTemplate, DEFAULT_THRESHOLD, DEFAULT_WINDOW);
    }

    public MfaLockoutService(StringRedisTemplate redisTemplate, int threshold, Duration window) {
        this.redisTemplate = Objects.requireNonNull(redisTemplate, "redisTemplate");
        if (threshold < 1) {
            throw new IllegalArgumentException("threshold must be >= 1");
        }
        this.threshold = threshold;
        this.window = Objects.requireNonNull(window, "window");
    }

    /**
     * Increment the failure counter for ({@code userType}, {@code userId}) and re-arm
     * the TTL. Returns the post-increment count so callers can decide whether to flip
     * the response into 423 territory in the same call.
     */
    public long recordFailure(String userType, String userId) {
        String key = key(userType, userId);
        Long count = redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, window);
        return count == null ? 0L : count;
    }

    /** Clear the failure counter — call on every successful verification. */
    public void clear(String userType, String userId) {
        redisTemplate.delete(key(userType, userId));
    }

    /**
     * Returns {@code true} when the counter has reached the lockout threshold. Lockout is
     * sticky for the remainder of the window: the threshold check is {@code count >= threshold},
     * and the TTL is re-armed on each failure so a flood pushes the unlock further out.
     */
    public boolean isLockedOut(String userType, String userId) {
        return currentCount(userType, userId) >= threshold;
    }

    /**
     * Seconds until the lockout naturally expires, suitable for an HTTP
     * {@code Retry-After} header. Returns {@code 0} when the subject is not locked out.
     */
    public long remainingSeconds(String userType, String userId) {
        if (!isLockedOut(userType, userId)) {
            return 0L;
        }
        Long seconds = redisTemplate.getExpire(key(userType, userId));
        return seconds == null || seconds < 0 ? 0L : seconds;
    }

    public int threshold() {
        return threshold;
    }

    /**
     * Configured lockout window in seconds — useful for callers that need a
     * {@code Retry-After} hint when a fresh threshold trip has just re-armed the TTL and
     * {@link #remainingSeconds(String, String)} would round to the full window anyway.
     */
    public long fallbackWindowSeconds() {
        return window.toSeconds();
    }

    private long currentCount(String userType, String userId) {
        String raw = redisTemplate.opsForValue().get(key(userType, userId));
        if (raw == null) {
            return 0L;
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }

    private String key(String userType, String userId) {
        Objects.requireNonNull(userType, "userType");
        Objects.requireNonNull(userId, "userId");
        return KEY_PREFIX + userType + ":" + userId;
    }
}
