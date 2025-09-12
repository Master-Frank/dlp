/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.security;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.topiam.employee.support.constant.EiamConstants;
import cn.topiam.employee.support.context.ServletContextService;
import cn.topiam.employee.support.enums.SecretType;
import cn.topiam.employee.support.result.ApiRestResult;
import cn.topiam.employee.support.util.AesUtils;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 获取加密key
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/5/20 21:32
 */
@RestController
@RequestMapping
public class PublicSecretEndpoint {

    public static final String PUBLIC_SECRET_PATH = EiamConstants.V1_API_PATH + "/public_secret";

    /**
     * 获取加密key
     *
     */
    @Validated
    @GetMapping(PUBLIC_SECRET_PATH)
    public ApiRestResult<PublicSecretResult> getPublicSecret(@RequestParam(value = "type", required = false) @NotNull(message = "秘钥类型不能为空") SecretType type) {
        //调用工具类生成秘钥
        String key = AesUtils.generateKey();
        //保存会话
        ServletContextService.getSession().setAttribute(type.getKey(), key);
        return ApiRestResult.ok(PublicSecretResult.builder().secret(key).build());
    }

    @Data
    @Builder
    public static class PublicSecretResult implements Serializable {

        @Serial
        private static final long serialVersionUID = 1868620270063512851L;

        /**
         * 秘钥
         */
        private String            secret;

    }
}
