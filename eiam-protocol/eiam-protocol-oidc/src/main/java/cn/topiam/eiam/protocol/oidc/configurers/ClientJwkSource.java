/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.configurers;

import java.util.List;
import java.util.Map;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import cn.topiam.employee.application.context.ApplicationContext;
import cn.topiam.employee.application.context.ApplicationContextHolder;
import cn.topiam.employee.application.oidc.model.OidcProtocolConfig;

/**
 * 客户端JWK
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/27 22:50
 */
public class ClientJwkSource implements JWKSource<SecurityContext> {

    @Override
    public List<JWK> get(JWKSelector jwkSelector,
                         SecurityContext context) throws KeySourceException {
        //根据应用编码获取应用证书
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        Map<String, Object> config = applicationContext.getConfig();
        OidcProtocolConfig protocolConfig = (OidcProtocolConfig) config
            .get(OidcProtocolConfig.class.getName());
        return jwkSelector.select(new JWKSet(protocolConfig.getJwks()));
    }
}
