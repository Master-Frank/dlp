package cn.topiam.employee.support.security.web;

import cn.topiam.employee.support.exception.UnknownAuthenticationTypeException;
import cn.topiam.employee.support.result.ApiRestResult;
import cn.topiam.employee.support.security.password.PasswordPolicyManager;
import cn.topiam.employee.support.security.password.authentication.NeedChangePasswordAuthenticationToken;
import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;
import cn.topiam.employee.support.security.password.exception.PasswordValidatedFailException;
import cn.topiam.employee.support.security.userdetails.UserDetails;
import cn.topiam.employee.support.security.userdetails.UserDetailsService;
import cn.topiam.employee.support.util.DesensitizationUtils;
import cn.topiam.employee.support.util.EmailUtils;
import cn.topiam.employee.support.util.HttpResponseUtils;
import cn.topiam.employee.support.web.decrypt.DecryptRequestBodyAdvice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 自定义认证过滤器
 * 用于处理用户认证请求
 */
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    @Generated
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
    private final UserDetailsService userDetailsService;
    private final PasswordPolicyManager passwordPolicyManager;
    private final SessionRegistry sessionRegistry;
    private final Executor taskExecutor;

    /**
     * 构造函数
     *
     * @param userDetailsService 用户详情服务
     * @param passwordPolicyManager 密码策略管理器
     * @param sessionRegistry 会话注册表
     * @param asyncConfigurer 异步配置器
     */
    public CustomAuthenticationFilter(UserDetailsService userDetailsService, PasswordPolicyManager passwordPolicyManager, SessionRegistry sessionRegistry, AsyncConfigurer asyncConfigurer) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.userDetailsService = userDetailsService;
        this.passwordPolicyManager = passwordPolicyManager;
        this.sessionRegistry = sessionRegistry;
        this.taskExecutor = asyncConfigurer.getAsyncExecutor();
    }

    /**
     * 尝试认证
     *
     * @param request HTTP请求
     * @return 认证对象
     * @throws AuthenticationException 认证异常
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            String decryptUsername = DecryptRequestBodyAdvice.decryptString(username);
            String decryptPassword = DecryptRequestBodyAdvice.decryptString(password);
            if (logger.isDebugEnabled()) {
                logger.debug("Processing authentication for username: {}", DesensitizationUtils.desensitizeUsername(decryptUsername));
            }

            if (Objects.isNull(decryptUsername)) {
                throw new BadCredentialsException("Username cannot be null");
            } else if (Objects.isNull(decryptPassword)) {
                throw new BadCredentialsException("Password cannot be null");
            } else {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(decryptUsername);
                if (Objects.isNull(userDetails)) {
                    throw new BadCredentialsException("Invalid username or password");
                } else {
                    try {
                        this.passwordPolicyManager.validate(decryptPassword, userDetails);
                    } catch (PasswordInvalidException var11) {
                        throw new BadCredentialsException(var11.getMessage());
                    }

                    if (!userDetails.isEnabled()) {
                        throw new DisabledException("User account is disabled");
                    } else if (!userDetails.isAccountNonLocked()) {
                        throw new LockedException("User account is locked");
                    } else {
                        try {
                            userDetails = this.userDetailsService.authenticate(decryptUsername, decryptPassword);
                        } catch (PasswordValidatedFailException var10) {
                            this.taskExecutor.execute(() -> {
                                this.passwordPolicyManager.recordFailedAttempt(decryptUsername);
                            });
                            throw new BadCredentialsException("Invalid username or password");
                        }

                        this.taskExecutor.execute(() -> {
                            this.passwordPolicyManager.recordSuccessfulAttempt(decryptUsername);
                        });
                        if (this.passwordPolicyManager.needChangePassword(userDetails)) {
                            return new NeedChangePasswordAuthenticationToken(userDetails);
                        } else {
                            return this.getAuthenticationManager().authenticate(this.createAuthenticationToken(userDetails, decryptPassword));
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取用户名
     *
     * @param request HTTP请求
     * @return 用户名
     */
    protected String obtainUsername(HttpServletRequest request) {
        Map<String, String> paramMap = this.getParams(request);
        return (String)paramMap.get("username");
    }

    /**
     * 获取密码
     *
     * @param request HTTP请求
     * @return 密码
     */
    protected String obtainPassword(HttpServletRequest request) {
        Map<String, String> paramMap = this.getParams(request);
        return (String)paramMap.get("password");
    }

    /**
     * 获取请求参数
     *
     * @param request HTTP请求
     * @return 参数映射
     */
    private Map<String, String> getParams(HttpServletRequest request) {
        try {
            String body = request.getReader().lines().reduce("", (accumulator, actual) -> {
                return accumulator + actual;
            });
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
            };
            return (Map)mapper.readValue(body, typeRef);
        } catch (Exception var6) {
            logger.error("Failed to parse request parameters", var6);
            return Map.of();
        }
    }

    /**
     * 创建认证令牌
     *
     * @param userDetails 用户详情
     * @param password 密码
     * @return 认证令牌
     */
    private Authentication createAuthenticationToken(UserDetails userDetails, String password) {
        return new CustomLoginFilter(userDetails, password, userDetails.getAuthorities());
    }

    /**
     * 认证成功
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param chain 过滤器链
     * @param authResult 认证结果
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult instanceof NeedChangePasswordAuthenticationToken) {
            NeedChangePasswordAuthenticationToken needChangePasswordAuthenticationToken = (NeedChangePasswordAuthenticationToken)authResult;
            ApiRestResult<Map<String, Object>> result = ApiRestResult.builder().status(String.valueOf(HttpStatus.UNAUTHORIZED.value())).message("Password needs to be changed").result(Map.of("needChangePassword", true, "principal", needChangePasswordAuthenticationToken.getPrincipal())).build();
            HttpResponseUtils.writeResponse(response, result, HttpStatus.UNAUTHORIZED);
        } else {
            super.successfulAuthentication(request, response, chain, authResult);
            this.sessionRegistry.registerNewSession(request.getSession(true).getId(), authResult.getPrincipal());
        }
    }

    /**
     * 认证失败
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param failed 失败的认证异常
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String errorMsg = "Invalid username or password";
        if (failed instanceof DisabledException) {
            errorMsg = "User account is disabled";
        } else if (failed instanceof LockedException) {
            errorMsg = "User account is locked";
        } else if (failed instanceof UnknownAuthenticationTypeException) {
            errorMsg = failed.getMessage();
        }

        ApiRestResult<Object> result = ApiRestResult.builder().status(String.valueOf(HttpStatus.UNAUTHORIZED.value())).message(errorMsg).build();
        HttpResponseUtils.writeResponse(response, result, HttpStatus.UNAUTHORIZED);
    }
}