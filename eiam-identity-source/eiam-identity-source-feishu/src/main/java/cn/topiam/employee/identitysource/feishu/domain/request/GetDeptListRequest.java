/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu.domain.request;

import com.alibaba.fastjson2.annotation.JSONField;

import cn.topiam.employee.identitysource.feishu.domain.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import static cn.topiam.employee.identitysource.feishu.FeiShuConstant.PAGE_SIZE;

/**
 * 部门列表入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 22:47
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GetDeptListRequest extends BaseRequest {
    /**
     * 是否递归获取子部门
     * 示例值：false
     */
    @JSONField(name = "fetch_child")
    private boolean fetchChild = false;
    /**
     * 分页大小
     * 示例值：10
     * 数据校验规则：
     * 最大值：50
     */
    @JSONField(name = "page_size")
    private int     pageSize   = PAGE_SIZE;
    /**
     * 分页标记，第一次请求不填，表示从头开始遍历；分页查询结果还有更多项时会同时返回新的 page_token，下次遍历可采用该 page_token 获取查询结果
     * 示例值："AQD9/Rn9eij9Pm39ED40/RD/cIFmu77WxpxPB/2oHfQLZ+G8JG6tK7+ZnHiT7COhD2hMSICh/eBl7cpzU6JEC3J7COKNe4jrQ8ExwBCR"
     */
    @JSONField(name = "page_token")
    private String  pageToken;
}
