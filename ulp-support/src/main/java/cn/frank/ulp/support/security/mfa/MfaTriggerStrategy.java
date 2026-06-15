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

import org.springframework.security.core.Authentication;

/**
 * Per-deployable decision: given a freshly-authenticated subject, should the success handler
 * commit the login, ask for a TOTP challenge, or force the subject through bind/setup first?
 *
 * <p>Implementations live in each deployable so they can consult the DAO matching their
 * subject type — the console strategy reads {@code ulp_administrator.mfa_enabled}, the
 * portal strategy additionally consults {@code OrgMfaPolicyService} for org-level
 * enforcement.
 *
 * <p>Strategies MUST short-circuit cheaply when the subject is not the type this deployable
 * cares about (e.g. console strategy returns {@link MfaDecision#DIRECT_LOGIN} for
 * non-{@code ADMIN} principals). Throwing on unexpected types would convert a normal login
 * into a 500.
 */
@FunctionalInterface
public interface MfaTriggerStrategy {

    /**
     * @param authentication the just-passed primary authentication (already populated with
     *                       {@code UserDetails} principal + {@code WebAuthenticationDetails})
     * @return one of {@link MfaDecision#DIRECT_LOGIN}, {@link MfaDecision#CHALLENGE_REQUIRED},
     *         {@link MfaDecision#SETUP_REQUIRED} — never null
     */
    MfaDecision decide(Authentication authentication);
}
