/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu.domain.response;

import java.io.Serializable;

import cn.topiam.employee.identitysource.feishu.domain.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门详情反参
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 23:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetDetailsResponse<T extends Serializable> extends BaseResponse {
    private DetailsData<T> data;

    /**
     * 详细信息
     */
    @Data
    public static class DetailsData<T extends Serializable> implements Serializable {
        private T department;
        private T user;
    }
}
