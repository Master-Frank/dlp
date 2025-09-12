/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.account;

import java.io.Serial;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 批量查询用户响应
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "批量查询用户响应")
public class BatchUserResult extends UserResult {
    @Serial
    private static final long serialVersionUID = -5144879825451360221L;
}
