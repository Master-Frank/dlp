/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.request;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 编辑用户入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:16
 */
@Data
@Schema(description = "修改用户入参")
public class UpdateUserInfoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -6616249172773611157L;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String            fullName;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String            nickName;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String            avatar;
}
