/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2024/04/11 21:30
 */
public class UserGroupNotExistException extends TopIamException {

    public UserGroupNotExistException() {
        this("用户组不存在");
    }

    public UserGroupNotExistException(String message) {
        super("user_group_not_exist", message, HttpStatus.BAD_REQUEST);
    }
}
