/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.security.mfa;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import cn.frank.ulp.common.repository.account.OrganizationMemberRepository;
import cn.frank.ulp.common.repository.account.OrganizationRepository;

/**
 * 组织级 MFA 强制策略判定。
 *
 * <p><b>语义</b>：用户隶属任一启用 {@code mfa_enforced} 的组织即视为被强制（OR 语义），
 * 不沿父链继承。Admin 调用路径不走此服务（admin 永远自愿，由 {@code ulp-console} 直接跳过）。
 *
 * <p><b>边界决策</b>（详见 add-mfa-totp-second-factor proposal）：
 * <ul>
 *   <li>多组织归属取 OR，避免"部分管理员关闭强制即可绕过"</li>
 *   <li>不沿父链继承，避免顶层组织一旦开启即穿透整棵树的爆炸半径</li>
 *   <li>grace = 0，立即生效，配合前端 {@code mfa_setup_required} 强拉到 {@code /mfa/setup}</li>
 * </ul>
 *
 * <p>短路语义：用户无组织成员关系 → 直接返回 false，不打 organization 表。
 */
@Service
public class OrgMfaPolicyService {

    private final OrganizationMemberRepository memberRepository;
    private final OrganizationRepository       organizationRepository;

    public OrgMfaPolicyService(OrganizationMemberRepository memberRepository,
                               OrganizationRepository organizationRepository) {
        this.memberRepository = Objects.requireNonNull(memberRepository, "memberRepository");
        this.organizationRepository = Objects.requireNonNull(organizationRepository,
            "organizationRepository");
    }

    /**
     * @param userId end-user id (NOT admin id — admin path skips this service entirely)
     * @return true iff the user belongs to at least one organization with
     *         {@code mfa_enforced = true}
     */
    public boolean isUserEnforced(String userId) {
        Objects.requireNonNull(userId, "userId");
        List<String> orgIds = memberRepository.findOrgIdsByUserId(userId);
        if (orgIds.isEmpty()) {
            return false;
        }
        return organizationRepository.existsByIdInAndMfaEnforcedTrue(orgIds);
    }
}
