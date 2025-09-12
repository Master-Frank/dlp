/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu.domain.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.topiam.employee.identitysource.feishu.domain.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 列表反参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 22:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetListResponse<T extends Serializable> extends BaseResponse {
    private ListData<T> data;

    /**
     * 列表信息
     * @param <T>
     */
    @Data
    public static class ListData<T extends Serializable> implements Serializable {
        /**
         * 是否还有更多项
         */
        @JsonProperty("has_more")
        private boolean hasMore;
        /**
         * 分页标记，当 has_more 为 true 时，会同时返回新的 page_token，否则不返回 page_token
         */
        @JsonProperty("page_token")
        private String  pageToken;
        /**
         * 部门列表
         */
        private List<T> items;
    }
}
