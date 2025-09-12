/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.jackson;

import org.springframework.security.jackson2.SecurityJackson2Modules;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import cn.topiam.employee.protocol.jwt.authentication.JwtAuthenticationToken;

/**
 * JwtAuthorizationModule
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/30 21:07
 */
public class JwtAuthorizationModule extends SimpleModule {

    public JwtAuthorizationModule() {
        super(JwtAuthorizationModule.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
        context.setMixInAnnotations(JwtAuthenticationToken.class,
            JwtAuthenticationTokenMixin.class);
    }

}
