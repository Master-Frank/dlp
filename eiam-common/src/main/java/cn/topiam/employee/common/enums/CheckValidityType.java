/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums;

/**
 * 检查有效性类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/17 21:22
 */
public enum CheckValidityType {
                               /**
                                * 邮箱
                                */
                               EMAIL,
                               /**
                                * 手机号
                                */
                               PHONE,
                               /**
                                * 名称
                                */
                               NAME,
                               /**
                                * 编码
                                */
                               CODE,
                               /**
                                * 用户名
                                */
                               USERNAME;
}
