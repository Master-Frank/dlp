/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取应用列表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/1 11:58
 */
@Data
@Schema(description = "获取分组应用列表")
public class AppGroupListResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1263170640092199401L;

    /**
     * 应用分组ID
     */
    @Schema(description = "应用分组ID")
    private String            id;

    /**
     * 应用分组名称
     */
    @Schema(description = "应用分组名称")
    private String            name;

    /**
     * APP数量
     */
    @Schema(description = "APP数量")
    private Integer           appCount;

}
