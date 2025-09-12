/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 批量获取组织结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "批量获取组织响应")
public class BatchOrganizationResult extends OrganizationResult {

}
