/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.exception;

import cn.topiam.employee.support.exception.TopIamException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 应用模版不存在
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:49
 */
public class AppTemplateNotExistException extends TopIamException {

    public AppTemplateNotExistException() {
        super("app_template_not_exist", "应用模版不存在", BAD_REQUEST);
    }
}
