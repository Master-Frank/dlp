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
/**
 * ULP 集成测试共享基础设施。
 *
 * <h2>包含什么</h2>
 * <ul>
 *   <li>{@link cn.frank.ulp.support.testsupport.AbstractIntegrationTest}
 *       —— 所有 IT 的根基类：{@code @SpringBootTest(MOCK)} + {@code @AutoConfigureMockMvc}
 *       + {@code @Transactional} 自动回滚 + singleton 模式的 MySQL / Redis 容器。</li>
 *   <li>{@link cn.frank.ulp.support.testsupport.RedisIntegrationTest}
 *       —— Redis 相关测试的子基类，提供 {@code @AfterEach flushDb()} 隔离 key 空间。</li>
 *   <li>{@link cn.frank.ulp.support.testsupport.SharedContainers}
 *       —— 集中管理容器 image tag 和默认配置，升级镜像版本时只改这一处。</li>
 * </ul>
 *
 * <h2>下游模块如何消费</h2>
 *
 * <p>这个包通过 {@code maven-jar-plugin} 的 {@code test-jar} execution
 * 打成单独的 jar，下游模块在 {@code pom.xml} 加：</p>
 *
 * <pre>{@code
 * <dependency>
 *     <groupId>cn.frank.ulp</groupId>
 *     <artifactId>ulp-support</artifactId>
 *     <version>${project.version}</version>
 *     <type>test-jar</type>
 *     <scope>test</scope>
 * </dependency>
 *
 * <!-- test-jar 不会传递 test-scope 依赖，下游必须再次声明 -->
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-test</artifactId>
 *     <scope>test</scope>
 * </dependency>
 * <dependency>
 *     <groupId>org.testcontainers</groupId>
 *     <artifactId>testcontainers</artifactId>
 *     <scope>test</scope>
 * </dependency>
 * <dependency>
 *     <groupId>org.testcontainers</groupId>
 *     <artifactId>mysql</artifactId>
 *     <scope>test</scope>
 * </dependency>
 * <dependency>
 *     <groupId>org.testcontainers</groupId>
 *     <artifactId>junit-jupiter</artifactId>
 *     <scope>test</scope>
 * </dependency>
 * }</pre>
 *
 * <p>然后在 {@code src/test/java} 写 {@code XxxControllerIT extends AbstractIntegrationTest}，
 * 命名以 {@code IT.java} 结尾即可被 Failsafe 在 {@code verify} 阶段拉起。</p>
 *
 * <h2>关键设计</h2>
 *
 * <p><strong>Singleton container 模式</strong> —— 不用 {@code @Testcontainers} +
 * {@code @Container} 注解，改用 static initializer 手动 {@code start()} 容器。原因：
 * {@code @Container static} 字段会在每个 test class 结束时 stop 容器、下一个 class 重新 start
 * （新端口），但 Spring TestContext 会缓存第一个 class 的 {@code ApplicationContext}
 * 给后续 class 复用，缓存里的 {@code DataSource} 仍指向旧端口，造成第二个 IT class 起
 * 全部 {@code Connection refused}。singleton 模式让容器在 JVM 生命周期内只启一次、
 * 端口稳定，由 Ryuk reaper 在 JVM 退出时统一清理。</p>
 *
 * <p><strong>{@code @DynamicPropertySource}</strong> —— 把容器 endpoint
 * （JDBC URL、Redis host/port）动态注入 Spring 环境，下游 IT 不需要写测试用的
 * {@code application.yml}。</p>
 *
 * <p><strong>{@code @Transactional}</strong> —— 每个测试方法在事务里执行、结束自动回滚，
 * 避免跨方法的 SQL 状态污染。注意：审计日志写入走 {@code REQUIRES_NEW}，不会被回滚。</p>
 *
 * @see cn.frank.ulp.support.testsupport.AbstractIntegrationTest
 * @see cn.frank.ulp.support.testsupport.SharedContainers
 */
package cn.frank.ulp.support.testsupport;
