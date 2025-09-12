/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.constant;

import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import static cn.topiam.employee.protocol.code.constant.ProtocolConstants.PROTOCOL_CACHE_PREFIX;
import static cn.topiam.employee.support.constant.EiamConstants.COLON;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/4 21:19
 */
public class OidcProtocolConstants {

    /**
     * 协议缓存前缀
     */
    public static final String          OIDC_PROTOCOL_CACHE_PREFIX = PROTOCOL_CACHE_PREFIX + "oidc"
                                                                     + COLON;

    public static final OAuth2TokenType ID_TOKEN                   = new OAuth2TokenType(
        "id_token");

    public static final String          OIDC_ERROR_URI             = "https://eiam.topiam.cn/docs/application/oidc/faq/#%E9%94%99%E8%AF%AF%E7%A0%81";

    public final class ConfigurationSettingNames {
        private static final String SETTINGS_NAMESPACE = "settings.";

        private ConfigurationSettingNames() {
        }

        /**
         * The names for token configuration settings.
         */
        public static final class Token {
            private static final String TOKEN_SETTINGS_NAMESPACE = SETTINGS_NAMESPACE
                .concat("token.");

            /**
             * id_token 扩展字段
             */
            public static final String  ID_TOKEN_CUSTOM_CLAIMS   = TOKEN_SETTINGS_NAMESPACE
                .concat("id-token-custom-claims");

            private Token() {
            }

        }

    }
}
