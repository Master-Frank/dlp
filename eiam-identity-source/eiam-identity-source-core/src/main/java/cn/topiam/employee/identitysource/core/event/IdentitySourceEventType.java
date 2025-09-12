/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.event;

/**
     * 身份源配置事件类型
     *
     * @author TopIAM
     * Created by support@topiam.cn on 2022/3/20 21:59
     */
public enum IdentitySourceEventType {
                                     /**
                                      * 注册
                                      */
                                     REGISTER,
                                     /**
                                      * 销毁
                                      */
                                     DESTROY,

                                     /**
                                      * 同步
                                      */
                                     SYNC
}