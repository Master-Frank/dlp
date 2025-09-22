package cn.topiam.employee.support.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 密钥类型
 *
 * @author TopIAM
 * Created by support on 2020/8/18 21:30
 */
@Getter
@AllArgsConstructor
public enum SecretType implements BaseEnum, Serializable {
    /**
     * 登录密钥
     */
    LOGIN("login", "TOPIAM_LOGIN_SECRET", "登录密钥"),

    /**
     * 加密密钥
     */
    ENCRYPT("encrypt", "TOPIAM_ENCRYPT_SECRET", "加密密钥");

    /**
     * code
     */
    @JsonValue
    private final String code;

    /**
     * 默认密钥
     */
    private final String defaultSecret;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据code获取类型
     *
     * @param code 代码
     * @return SecretType
     */
    public static SecretType getType(String code) {
        for (SecretType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new RuntimeException("未找到密钥类型");
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}