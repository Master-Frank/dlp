/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserAgent
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAgent implements Serializable {

    public static final String USER_AGENT_BROWSER = "user_agent.browser.keyword";

    private String             deviceType;

    private String             platform;

    private String             platformVersion;

    private String             browser;

    private String             browserType;

    private String             browserMajorVersion;
}
