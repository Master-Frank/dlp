/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.result;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * LoginConfigResult
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/13 21:29
 */
@Builder
@Data
public class LoginConfigResult implements Serializable {

    @Serial
    private static final long            serialVersionUID = 7255002979319970337L;

    /**
     * idps
     */
    @Parameter(description = "IDPS")
    private List<IdentityProviderResult> idps;
}
