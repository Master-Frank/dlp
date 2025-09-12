/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.security.password.task;

/**
 * 密码任务
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/18 23:43
 */
public interface PasswordExpireTask {
    /**
     * 执行
     */
    void execute();
}
