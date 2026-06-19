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
 * 极窄查询接口：仅暴露"该用户名是否启用了 MFA"一个判定，供需要在非交互式登录链路里短路掉
 * 已启用第二因子用户的提供者使用（典型场景：ROPC password grant）。
 *
 * <p>选择独立接口而不是扩展 {@link cn.frank.ulp.support.security.userdetails.UserDetails}：
 * <ul>
 *   <li>{@code UserDetails} 已经是 30+ 字段的复合 DTO，再加字段会破坏 Jackson 序列化/反序列化兼容</li>
 *   <li>查询路径与 {@code UserDetailsService} 解耦——一些上游 UDS 实现并不感知 mfa 字段</li>
 *   <li>新增 bean 可由部署侧按需注入；不提供时 ROPC 行为退化为"不拒绝"，向下兼容</li>
 * </ul>
 *
 * <p>实现要点：
 * <ul>
 *   <li>{@code username} 由调用方传入主认证返回的 principal name（即 canonical username）</li>
 *   <li>未找到用户、字段为 {@code null} → 返回 {@code false}，不抛异常</li>
 *   <li>不应抛出 checked/unchecked 异常给调用方；任何持久层故障应在内部吞掉并记录日志</li>
 * </ul>
 *
 * @author Frank Zhang
 */
public interface MfaStatusLookup {

    /**
     * 判断指定用户是否已经启用 MFA（即应当被拒绝走非交互式 password grant 这类无第二因子凭据的登录路径）。
     *
     * @param username canonical username
     * @return {@code true} 表示该用户已启用 MFA；{@code false} 表示未启用或用户不存在
     */
    boolean isMfaEnabled(String username);
}
