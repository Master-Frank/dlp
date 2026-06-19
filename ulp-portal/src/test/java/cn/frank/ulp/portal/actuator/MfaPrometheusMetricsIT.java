/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.actuator;

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
 * Phase 6.9 — verifies that the portal deployable's {@code /actuator/prometheus} exposition
 * carries the four {@code ulp_mfa_*} metric families wired by
 * {@link cn.frank.ulp.portal.configuration.security.PortalSecurityConfiguration#mfaMetrics}.
 *
 * <p>Counters are only published in the exposition after their first {@code increment()} call
 * (Micrometer's Prometheus registry registers Counters lazily). This test fires one sample per
 * family via the autowired {@link MfaMetrics} bean before scraping — that's the cheap way to
 * prove "the bean exists, the names are correct, the tag schema matches". Production traffic
 * does the real driving.
 *
 * <p>The {@code ulp_mfa_pending_active} gauge is registered eagerly in the {@code MfaMetrics}
 * constructor (see {@link MfaMetrics#MfaMetrics}), so it shows up in the exposition without
 * any synthetic activity.
 *
 * <p>Tag values picked here mirror the actual subject/phase/outcome strings the controllers
 * pass at runtime — see {@code MfaController} / {@code MfaChallengeController} call sites. Any
 * future rename in the wrapper API will break this test, which is the intended early-warning
 * signal for the dashboards downstream.
 */
@ActiveProfiles("test")
class MfaPrometheusMetricsIT extends AbstractMfaIntegrationTest {

    @Autowired
    private MfaMetrics mfaMetrics;

    @Test
    void prometheusExpositionContainsAllFourMfaFamilies() throws Exception {
        mfaMetrics.verifyOutcome("user", "totp", MfaChallengeOutcome.SUCCESS);
        mfaMetrics.lockout("user", "challenge");
        mfaMetrics.bind("user", "confirm", "success");

        // /actuator/health first so WebMvcMetricsFilter has populated jvm_/http_ baseline.
        mockMvc.perform(get("/actuator/health"));
        MvcResult result = mockMvc.perform(get("/actuator/prometheus")).andReturn();
        assertThat(result.getResponse().getStatus()).as("/actuator/prometheus 公开端点应返回 200")
            .isEqualTo(200);
        String body = result.getResponse().getContentAsString();

        assertThat(body).as("prometheus 抓取必须包含 portal 部署单元注册的 4 个 ulp_mfa_* 指标家族")
            .contains("ulp_mfa_verify_total").contains("ulp_mfa_lockout_total")
            .contains("ulp_mfa_bind_total").contains("ulp_mfa_pending_active");

        assertThat(body).as("ulp_mfa_verify_total 必须带 subject_type/via/outcome 三个 tag")
            .contains("subject_type=\"user\"").contains("via=\"totp\"")
            .contains("outcome=\"success\"");
        assertThat(body).as("ulp_mfa_lockout_total 必须带 phase tag").contains("phase=\"challenge\"");
        assertThat(body).as("ulp_mfa_bind_total 必须带 phase=confirm tag")
            .contains("phase=\"confirm\"");
    }
}
