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

import java.io.Serializable;
import java.time.Instant;

import org.springframework.security.core.Authentication;

/**
 * Serializable envelope for a pending MFA challenge: the just-authenticated
 * {@link Authentication}, the source IP captured at primary-auth time (for /24 same-subnet
 * verification at challenge time), and a creation timestamp for diagnostics.
 *
 * <p>Persisted as a single Redis value under {@code ULP_MFA_PENDING:{uuid}} with a 5-minute
 * TTL. Serialized via the {@code springSessionDefaultRedisSerializer} bean (Jackson 3 +
 * {@code AuthenticationJacksonModule}) — the same one Spring Session uses, so the
 * {@link Authentication} round-trips identically to the Spring Session principal.
 *
 * <p>Mutable POJO with a no-arg constructor on purpose:
 * {@link org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer}
 * embeds class info and instantiates via the default ctor + setters. Don't migrate this
 * to a record without first proving the serializer can rebuild it across a Redis round-trip.
 */
public class MfaPendingEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private Authentication    authentication;

    private String            sourceIp;

    private Instant           createdAt;

    public MfaPendingEntry() {
    }

    public MfaPendingEntry(Authentication authentication, String sourceIp, Instant createdAt) {
        this.authentication = authentication;
        this.sourceIp = sourceIp;
        this.createdAt = createdAt;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
