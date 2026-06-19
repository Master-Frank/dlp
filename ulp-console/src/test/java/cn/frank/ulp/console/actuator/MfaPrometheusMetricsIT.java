/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import cn.frank.ulp.support.security.mfa.MfaChallengeOutcome;
import cn.frank.ulp.support.security.mfa.MfaMetrics;
import cn.frank.ulp.support.testsupport.AbstractMfaIntegrationTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Phase 6.9 — verifies that the console deployable's {@code /actuator/prometheus} exposition
 * carries the four {@code ulp_mfa_*} metric families wired by
 * {@link cn.frank.ulp.console.configuration.ConsoleSecurityConfiguration#mfaMetrics}.
 *
 * <p>Mirrors {@code cn.frank.ulp.portal.actuator.MfaPrometheusMetricsIT} — the subject_type
 * differs ({@code admin} on console, {@code user} on portal) because each SecurityConfiguration
 * wires its own MfaMetrics bean against the deployable's principal type. The two test classes
 * stay independent (instead of extending a shared abstract base) so that the chosen tag values
 * stay readable at the call site and per-deployable schema changes can land without touching
 * the other side.
 */
@ActiveProfiles("test")
class MfaPrometheusMetricsIT extends AbstractMfaIntegrationTest {

    @Autowired
    private MfaMetrics mfaMetrics;

    @Test
    void prometheusExpositionContainsAllFourMfaFamilies() throws Exception {
        mfaMetrics.verifyOutcome("admin", "totp", MfaChallengeOutcome.SUCCESS);
        mfaMetrics.lockout("admin", "challenge");
        mfaMetrics.bind("admin", "confirm", "success");

        // /actuator/health first so WebMvcMetricsFilter has populated jvm_/http_ baseline.
        mockMvc.perform(get("/actuator/health"));
        MvcResult result = mockMvc.perform(get("/actuator/prometheus")).andReturn();
        assertThat(result.getResponse().getStatus()).as("/actuator/prometheus 公开端点应返回 200")
            .isEqualTo(200);
        String body = result.getResponse().getContentAsString();

        assertThat(body).as("prometheus 抓取必须包含 console 部署单元注册的 4 个 ulp_mfa_* 指标家族")
            .contains("ulp_mfa_verify_total").contains("ulp_mfa_lockout_total")
            .contains("ulp_mfa_bind_total").contains("ulp_mfa_pending_active");

        assertThat(body).as("ulp_mfa_verify_total 必须带 subject_type=admin tag")
            .contains("subject_type=\"admin\"").contains("via=\"totp\"")
            .contains("outcome=\"success\"");
        assertThat(body).as("ulp_mfa_lockout_total 必须带 phase tag").contains("phase=\"challenge\"");
        assertThat(body).as("ulp_mfa_bind_total 必须带 phase=confirm tag")
            .contains("phase=\"confirm\"");
    }
}
