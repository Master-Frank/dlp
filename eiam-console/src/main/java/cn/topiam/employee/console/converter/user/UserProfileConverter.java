/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.converter.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import cn.topiam.employee.common.entity.account.UserEntity;
import cn.topiam.employee.common.entity.setting.AdministratorEntity;
import cn.topiam.employee.console.pojo.update.user.UpdateUserInfoRequest;

/**
 * AccountConverter
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/25 21:52
 */
@Mapper(componentModel = "spring")
public interface UserProfileConverter {

    /**
     * 用户更新参数转换为用户实体类
     *
     * @param param {@link UpdateUserInfoRequest} 更新参数
     * @return {@link UserEntity} 用户实体
     */
    @Mapping(target = "needChangePassword", ignore = true)
    @Mapping(target = "phoneVerified", ignore = true)
    @Mapping(target = "phoneAreaCode", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastUpdatePasswordTime", ignore = true)
    @Mapping(target = "lastAuthTime", ignore = true)
    @Mapping(target = "lastAuthIp", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expand", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "authTotal", ignore = true)
    AdministratorEntity userUpdateParamConvertToAdministratorEntity(UpdateUserInfoRequest param);
}
