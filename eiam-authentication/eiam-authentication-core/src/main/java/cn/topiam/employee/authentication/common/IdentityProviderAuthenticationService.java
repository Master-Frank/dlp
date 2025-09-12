/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common;

import cn.topiam.employee.authentication.common.authentication.IdentityProviderUserDetails;
import cn.topiam.employee.support.security.userdetails.UserDetails;

/**
 * UserIdpService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 21:54
 */
public interface IdentityProviderAuthenticationService {

    /**
     * 检查用户已绑定对应平台
     *
     * @param openId     {@link  String}
     * @param providerId {@link  String}
     * @return {@link  Boolean}
     */
    Boolean checkIdpUserIsExistBind(String openId, String providerId);

    /**
     * 绑定
     *
     * @param accountId   {@link  String} 账户ID
     * @param identityProviderUserDetails   {@link  IdentityProviderUserDetails} 用户信息
     * @return {@link  Boolean}
     */
    Boolean bindUserIdp(String accountId, IdentityProviderUserDetails identityProviderUserDetails);

    /**
     * 更新账户信息
     *
     * @param identityProviderUserDetails   {@link IdentityProviderUserDetails} 用户信息
     * @param providerId {@link  String} 提供商ID
     * @return {@link  Boolean}
     */
    Boolean updateThirdPartyUser(IdentityProviderUserDetails identityProviderUserDetails,
                                 String providerId);

    /**
     * 获取用户详情
     *
     * @param openId     {@link String}
     * @param providerId {@link String}
     * @return {@link UserDetails}
     */
    UserDetails getUserDetails(String openId, String providerId);
}
