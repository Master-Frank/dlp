/*
 * eiam-application-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.form.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import cn.topiam.employee.application.form.pojo.AppFormConfigGetResult;
import cn.topiam.employee.application.form.pojo.AppFormProtocolEndpoint;
import cn.topiam.employee.application.form.pojo.AppFormSaveConfigParam;
import cn.topiam.employee.common.entity.app.AppFormConfigEntity;
import cn.topiam.employee.common.entity.app.po.AppFormConfigPO;
import cn.topiam.employee.core.context.ContextService;
import static cn.topiam.employee.common.constant.ProtocolConstants.APP_CODE;
import static cn.topiam.employee.common.constant.ProtocolConstants.FormEndpointConstants.FORM_SSO_PATH;

/**
 * 应用映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface AppFormConfigConverter {

    /**
     * save 转 entity
     *
     * @param config {@link AppFormSaveConfigParam}
     * @return {@link AppFormConfigEntity}
     */

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "appId", ignore = true)
    AppFormConfigEntity appFormSaveConfigParamToEntity(AppFormSaveConfigParam config);

    /**
     * po 转 result
     *
     * @param po {@link AppFormConfigPO}
     * @return {@link AppFormConfigGetResult}
     */
    default AppFormConfigGetResult entityConverterToFormConfigResult(AppFormConfigPO po) {
        if (po == null) {
            return null;
        }
        AppFormConfigGetResult result = new AppFormConfigGetResult();
        if (po.getAppId() != null) {
            result.setAppId(po.getAppId());
        }
        result.setInitLoginUrl(po.getInitLoginUrl());
        result.setLoginUrl(po.getLoginUrl());
        result.setUsernameField(po.getUsernameField());
        result.setPasswordField(po.getPasswordField());
        result.setPasswordEncryptType(po.getPasswordEncryptType());
        result.setPasswordEncryptKey(po.getPasswordEncryptKey());
        result.setUsernameEncryptType(po.getUsernameEncryptType());
        result.setUsernameEncryptKey(po.getUsernameEncryptKey());
        result.setSubmitType(po.getSubmitType());
        List<AppFormConfigEntity.OtherField> list = po.getOtherField();
        if (list != null) {
            result.setOtherField(new ArrayList<>(list));
        }
        result.setProtocolEndpoint(getProtocolEndpointDomain(po.getAppCode()));
        return result;
    }

    /**
     * 获取协议端点
     *
     * @param appCode {@link String}
     * @return {@link AppFormProtocolEndpoint}
     */
    private AppFormProtocolEndpoint getProtocolEndpointDomain(String appCode) {
        //@formatter:off
        AppFormProtocolEndpoint domain = new AppFormProtocolEndpoint();
        Map<String,String> variables = new HashMap<>(16);
        variables.put(APP_CODE,appCode);
        StringSubstitutor sub = new StringSubstitutor(variables, "{", "}");
        //IDP SSO 端点
        domain.setIdpSsoEndpoint(sub.replace(ContextService.getPortalPublicBaseUrl()+FORM_SSO_PATH));
        return domain;
        //@formatter:on
    }
}
