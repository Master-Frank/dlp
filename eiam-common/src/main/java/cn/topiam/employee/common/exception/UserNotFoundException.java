/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 未找到用户异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/8 22:09
 */
public class UserNotFoundException extends TopIamException {
    public UserNotFoundException() {
        super("user_not_found_error", "未找到用户异常", HttpStatus.UNAUTHORIZED);
    }
}
