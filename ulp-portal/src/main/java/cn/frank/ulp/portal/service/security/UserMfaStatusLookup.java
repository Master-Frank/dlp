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
package cn.frank.ulp.portal.service.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.security.mfa.MfaStatusLookup;

import lombok.RequiredArgsConstructor;

/**
 * Portal 端 {@link MfaStatusLookup} 实现：查询 {@code ulp_user.mfa_enabled} 字段。
 *
 * <p>该 bean 一旦注册进 portal Spring 上下文（即此模块），
 * {@link cn.frank.ulp.protocol.oidc.configurers.OAuth2TokenEndpointConfigurer#createDefaultAuthenticationProviders}
 * 会通过 {@code getOptionalBean(...)} 自动拾取，从而让 ROPC 提供商在 MFA 已启用的账号上
 * 直接 401/{@code invalid_grant}，阻断"密码 grant 绕过第二因子"的攻击面。
 *
 * <p>console / openapi 不挂 OAuth2 AS，因此不会触发该路径；管理员 MFA 由 console 自己的
 * 表单登录 + 挑战端点保护，不走此 bean。
 *
 * <p>语义约束：
 * <ul>
 *   <li>空白用户名 → {@code false}（不抛异常，由调用方决定后续）</li>
 *   <li>未找到用户 → {@code false}（principal 名能拿到说明 UDS 已经放行，不属于本接口的责任范围）</li>
 *   <li>字段为 {@code null} → {@code false}（与 entity 默认值语义一致）</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class UserMfaStatusLookup implements MfaStatusLookup {

    private final UserRepository userRepository;

    @Override
    public boolean isMfaEnabled(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        return userRepository.findByUsername(username).map(UserEntity::getMfaEnabled)
            .orElse(Boolean.FALSE);
    }
}
