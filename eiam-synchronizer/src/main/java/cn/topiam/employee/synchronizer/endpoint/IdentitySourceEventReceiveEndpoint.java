/*
 * eiam-synchronizer - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.synchronizer.endpoint;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.topiam.employee.common.entity.identitysource.IdentitySourceEntity;
import cn.topiam.employee.common.repository.identitysource.IdentitySourceRepository;
import cn.topiam.employee.identitysource.core.IdentitySource;
import cn.topiam.employee.identitysource.core.IdentitySourceConfig;
import cn.topiam.employee.support.trace.Trace;
import cn.topiam.employee.synchronizer.configuration.IdentitySourceBeanUtils;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import static cn.topiam.employee.common.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;

/**
 * 身份源回调事件端点
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/20 21:24
 */
@Slf4j
@RestController
@RequestMapping(EVENT_RECEIVE_PATH)
@SuppressWarnings("unchecked")
public class IdentitySourceEventReceiveEndpoint {

    /**
     * 事件通知处理
     *
     * @param request {@link  HttpServletRequest}
     * @param code {@link  String}
     * @param body {@link  String}
     * @return {@link  ResponseEntity}
     */
    @Trace
    @RequestMapping(value = "/{code}")
    public ResponseEntity<?> receive(HttpServletRequest request, @PathVariable String code,
                                     @RequestBody(required = false) String body) {
        Optional<IdentitySourceEntity> optional = identitySourceRepository.findByCode(code);
        if (optional.isPresent()) {
            IdentitySourceEntity entity = optional.get();
            //已经启用&已经配置
            if (entity.getEnabled() && entity.getConfigured()) {
                String beanName = IdentitySourceBeanUtils.getSourceBeanName(optional.get().getId());
                IdentitySource<IdentitySourceConfig> identitySource = (IdentitySource<IdentitySourceConfig>) applicationContext
                    .getBean(beanName);
                Object event = identitySource.event(request, body);
                return ResponseEntity.ok(event);
            }
        }
        return ResponseEntity.ok().build();
    }

    private final ApplicationContext       applicationContext;

    private final IdentitySourceRepository identitySourceRepository;

    public IdentitySourceEventReceiveEndpoint(ApplicationContext applicationContext,
                                              IdentitySourceRepository identitySourceRepository) {
        this.applicationContext = applicationContext;
        this.identitySourceRepository = identitySourceRepository;
    }
}
