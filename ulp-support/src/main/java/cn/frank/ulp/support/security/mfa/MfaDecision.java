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
 * Outcome of {@link MfaTriggerStrategy#decide} — what {@link MfaAwareAuthenticationSuccessHandler}
 * should do after primary credentials verify.
 *
 * <p>Phase 3 (console) only emits {@link #DIRECT_LOGIN} or {@link #CHALLENGE_REQUIRED}.
 * {@link #SETUP_REQUIRED} is reserved for Phase 4 portal org-enforcement — when a user is
 * subject to org-level {@code mfa_enforced=true} but has not yet bound, they MUST be
 * redirected through the setup flow before any other resource is accessible.
 */
public enum MfaDecision {

                         /** Primary credentials are sufficient; commit the authentication and return success. */
                         DIRECT_LOGIN,

                         /**
                          * Subject has bound MFA — stash the {@link org.springframework.security.core.Authentication}
                          * in pending storage, do NOT commit to the session, and require a TOTP / backup-code
                          * challenge before login completes.
                          */
                         CHALLENGE_REQUIRED,

                         /**
                          * Subject is required by org policy to enable MFA but has not bound yet. The session
                          * MAY be committed (so the user can hit {@code /mfa/setup}) but every other route is
                          * gated by {@code OrgMfaEnforcementFilter} until {@code mfa_enabled=true}. Console
                          * strategies MUST NOT return this value — admins are voluntary.
                          */
                         SETUP_REQUIRED
}
