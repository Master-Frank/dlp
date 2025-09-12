/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.enums;

/**
 * 事件通知类型
 *
 * @author TopIAM
 */
public enum IdentitySourceEventReceiveType {
                                            /**
                                             * 用户添加
                                             */
                                            USER_ADD,
                                            /**
                                             * 用户修改
                                             */
                                            USER_MODIFY,
                                            /**
                                             * 用户离职
                                             */
                                            USER_LEAVE,

                                            /**
                                             * 用户删除
                                             */
                                            USER_REMOVE,

                                            /**
                                             * 部门创建
                                             */
                                            DEPT_CREATE,

                                            /**
                                             * 部门修改
                                             */
                                            DEPT_MODIFY,
                                            /**
                                             * 部门删除
                                             */
                                            DEPT_REMOVE
}
