/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.constant;

import static cn.topiam.employee.protocol.code.constant.ProtocolConstants.PROTOCOL_CACHE_PREFIX;
import static cn.topiam.employee.support.constant.EiamConstants.COLON;

/**
 * 协议常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/8 21:29
 */
public class JwtProtocolConstants {

    /**
     * 协议缓存前缀
     */
    public static final String JWT_PROTOCOL_CACHE_PREFIX = PROTOCOL_CACHE_PREFIX + "jwt" + COLON;

    public static final String TARGET_URL                = "target_url";
    public static final String ID_TOKEN                  = "id_token";
    public static final String NONCE                     = "nonce";

    public static final String URL                       = "url";
    public static final String BINDING_TYPE              = "binding_type";

    public static final String JWT_ERROR_URI             = "https://eiam.topiam.cn";

    public static final String POST_LOGOUT_REDIRECT_URI  = "post_logout_redirect_uri";

}
