/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.endpoint.pojo;

import java.util.Set;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 审计字典结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/27 22:35
 */
@Data
@Schema(description = "字典响应")
@ParameterObject
public class DictResult {

    @Parameter(description = "分组名")
    private String         name;
    @Parameter(description = "分组编码")
    private String         code;
    @Parameter(description = "类型")
    private Set<AuditType> types;

    @Data
    @Schema(description = "审计类型")
    public static class AuditType {
        /**
         * 名称
         */
        @Parameter(description = "名称")
        private String name;

        /**
         * CODE
         */
        @Parameter(description = "CODE")
        private String code;
    }

}
