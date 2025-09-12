/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.topiam.eiam.protocol.oidc.jackson;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import com.fasterxml.jackson.databind.Module;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/1/8 23:58
 */
public class OidcProtocolJackson2Module {
    public OidcProtocolJackson2Module() {
    }

    public static List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        modules.add(new OAuth2AuthorizationModule());
        modules.add(new OAuth2AuthorizationServerJackson2Module());
        return modules;
    }
}
