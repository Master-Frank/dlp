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
package cn.frank.ulp.support.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import cn.frank.ulp.support.security.mfa.MfaProperties;

/**
 * 为消费 ulp-support 的下游模块注册 {@link SupportProperties} 与 {@link MfaProperties}。
 *
 * <p>历史背景：原 eiam-support 私有 jar 里有一个等价的 auto-configuration，去 TOPIAM 反编译之后丢了；
 * 因为 SupportProperties 仅用 {@code @ConfigurationProperties} 注解（不是 stereotype），
 * 没人显式 {@code @EnableConfigurationProperties} 它的话生产里 OIDC authorize 链路会在第一次访问
 * {@code ContextService.getPortalPublicBaseUrl()} 时炸 NoSuchBeanDefinitionException。
 * 通过 {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}
 * 注册，确保任何依赖 ulp-support 的 Spring Boot app 都自动拿到 SupportProperties bean。</p>
 *
 * <p>注：RuntimeBaselineInfoContributor 用 {@code @Component} 自注册，不在此 @Bean
 * 注册——三个 deployable 的 Application 都在 {@code cn.frank.ulp} 包根，默认 component scan
 * 覆盖整个 {@code cn.frank.ulp.**} 子树。</p>
 */
@AutoConfiguration
@EnableConfigurationProperties({ SupportProperties.class, MfaProperties.class })
public class SupportAutoConfiguration {
}
