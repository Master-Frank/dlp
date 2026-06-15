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
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alibaba.fastjson2.JSON;

import cn.frank.ulp.support.exception.BadParamsException;

/**
 * Shared lifecycle for {@link MfaService} — keeps Redis staging, cipher round-trip,
 * OTP verification, and backup-code hashing in one place so the two concrete subtypes
 * (administrator in {@code ulp-console}, end-user in {@code ulp-portal}) only have to
 * supply DAO callbacks.
 *
 * <p>Subclasses MUST provide a non-empty {@link #subjectType()} string ({@code "admin"} or
 * {@code "user"}); it both namespaces the Redis key and disambiguates audit/log lines.
 */
public abstract class AbstractMfaService implements MfaService {

    private static final String            STAGING_KEY_PREFIX = "ULP_BIND_MFA_SECRET:";
    private static final Duration          STAGING_TTL        = Duration.ofMinutes(10);

    protected final MfaSecretGenerator     secretGenerator;
    protected final MfaSecretCipher        secretCipher;
    protected final MfaCodeVerifier        codeVerifier;
    protected final MfaOtpAuthUriBuilder   uriBuilder;
    protected final MfaBackupCodeGenerator backupCodeGenerator;
    protected final PasswordEncoder        passwordEncoder;
    protected final StringRedisTemplate    redisTemplate;

    protected AbstractMfaService(MfaSecretGenerator secretGenerator, MfaSecretCipher secretCipher,
                                 MfaCodeVerifier codeVerifier, MfaOtpAuthUriBuilder uriBuilder,
                                 MfaBackupCodeGenerator backupCodeGenerator,
                                 PasswordEncoder passwordEncoder,
                                 StringRedisTemplate redisTemplate) {
        this.secretGenerator = Objects.requireNonNull(secretGenerator);
        this.secretCipher = Objects.requireNonNull(secretCipher);
        this.codeVerifier = Objects.requireNonNull(codeVerifier);
        this.uriBuilder = Objects.requireNonNull(uriBuilder);
        this.backupCodeGenerator = Objects.requireNonNull(backupCodeGenerator);
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
        this.redisTemplate = Objects.requireNonNull(redisTemplate);
    }

    @Override
    public MfaPrepareBindResult prepareBind(String userId) {
        Objects.requireNonNull(userId, "userId");
        String username = loadUsername(userId);
        if (isMfaEnabled(userId)) {
            throw new IllegalStateException("MFA is already enabled for this subject");
        }
        String secretBase32 = secretGenerator.generate();
        String uri = uriBuilder.build(username, secretBase32);
        redisTemplate.opsForValue().set(stagingKey(userId), secretBase32, STAGING_TTL);
        return new MfaPrepareBindResult(uri, secretBase32);
    }

    @Override
    public MfaConfirmBindResult confirmBind(String userId, String otp) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(otp, "otp");
        String key = stagingKey(userId);
        String secretBase32 = redisTemplate.opsForValue().get(key);
        if (secretBase32 == null) {
            throw new IllegalStateException(
                "No pending MFA bind for this subject — call prepareBind first or the staging entry expired");
        }
        if (!codeVerifier.isValid(secretBase32, otp)) {
            throw new BadParamsException("invalid OTP");
        }
        String cipher = secretCipher.encrypt(secretBase32.getBytes(StandardCharsets.UTF_8));
        List<String> plaintextCodes = backupCodeGenerator.generate();
        List<String> hashedCodes = plaintextCodes.stream().map(passwordEncoder::encode).toList();
        String backupCodesJson = JSON.toJSONString(hashedCodes);
        persistEnabled(userId, cipher, backupCodesJson);
        redisTemplate.delete(key);
        return new MfaConfirmBindResult(plaintextCodes);
    }

    @Override
    public void unbind(String userId, String currentOtp) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(currentOtp, "currentOtp");
        String cipher = loadCipher(userId);
        if (cipher == null) {
            throw new IllegalStateException("MFA is not enabled for this subject");
        }
        String secretBase32 = new String(secretCipher.decrypt(cipher), StandardCharsets.UTF_8);
        if (!codeVerifier.isValid(secretBase32, currentOtp)) {
            throw new BadParamsException("invalid OTP");
        }
        persistDisabled(userId);
    }

    @Override
    public String loadActiveCipher(String userId) {
        Objects.requireNonNull(userId, "userId");
        return loadCipher(userId);
    }

    protected String stagingKey(String userId) {
        return STAGING_KEY_PREFIX + subjectType() + ":" + userId;
    }

    /** {@code "admin"} or {@code "user"} — namespaces Redis staging key. */
    @Override
    public abstract String subjectType();

    /** Throws {@link BadParamsException} if the subject does not exist. */
    protected abstract String loadUsername(String userId);

    protected abstract boolean isMfaEnabled(String userId);

    /** Returns null when MFA is not enabled. */
    protected abstract String loadCipher(String userId);

    protected abstract void persistEnabled(String userId, String cipher, String backupCodesJson);

    protected abstract void persistDisabled(String userId);
}
