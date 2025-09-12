/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 邮件提供商配置
 *
 * @author TopIAM
 */
public enum MailProvider implements BaseEnum {

                                              /**
                                               * 自定义
                                               */
                                              CUSTOMIZE("customize", "自定义", null, null, null),
                                              /**
                                               * 腾讯企业邮
                                               */
                                              TENCENT("tencent", "腾讯企业邮", "smtp.exmail.qq.com", 25,
                                                      465),
                                              /**
                                               * 阿里企业邮
                                               */
                                              ALIYUN("aliyun", "阿里企业邮", "smtp.mxhichina.com", 25,
                                                     465),
                                              /**
                                               * 网易
                                               */
                                              NETEASE("netease", "网易企业邮", "smtp.163.com", 25, 465);

    /**
     * code
     */
    @JsonValue
    private final String  code;
    /**
     * 名称
     */
    private final String  name;
    /**
     * smtp 地址
     */
    private final String  smtpUrl;
    /**
     * 端口
     */
    private final Integer port;
    /**
     * SSL 端口
     */
    private final Integer sslPort;

    MailProvider(String code, String name, String smtpUrl, Integer port, Integer sslPort) {
        this.code = code;
        this.name = name;
        this.smtpUrl = smtpUrl;
        this.port = port;
        this.sslPort = sslPort;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return null;
    }

    public String getName() {
        return name;
    }

    @EnumConvert
    public static MailProvider getType(String code) {
        MailProvider[] values = values();
        for (MailProvider status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该平台");
    }

    public String getSmtpUrl() {
        return smtpUrl;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getSslPort() {
        return sslPort;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
