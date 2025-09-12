/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.po;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.topiam.employee.common.entity.app.AppEntity;
import cn.topiam.employee.common.enums.app.AppProtocol;
import cn.topiam.employee.common.enums.app.AppType;
import cn.topiam.employee.common.enums.app.AuthorizationType;
import cn.topiam.employee.support.security.userdetails.Application;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用账户po
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/10 22:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppPO extends AppEntity {

    /**
     * 应用分组
     */
    private List<Application.ApplicationGroup> group;

    public AppPO(String id, String name, String code, String clientId, String clientSecret,
                 String template, AppProtocol protocol, AppType type, String icon,
                 String initLoginUrl, AuthorizationType authorizationType, Boolean enabled,
                 String groupIds) {
        super.setId(id);
        super.setName(name);
        super.setCode(code);
        super.setClientId(clientId);
        super.setClientSecret(clientSecret);
        super.setTemplate(template);
        super.setProtocol(protocol);
        super.setType(type);
        super.setIcon(icon);
        super.setInitLoginUrl(initLoginUrl);
        super.setAuthorizationType(authorizationType);
        super.setEnabled(enabled);
        if (StringUtils.isNotBlank(groupIds)) {
            this.group = new ArrayList<>();
            for (String groupId : groupIds.split(",")) {
                this.group.add(new Application.ApplicationGroup(groupId, null, null));
            }
        }
    }
}
