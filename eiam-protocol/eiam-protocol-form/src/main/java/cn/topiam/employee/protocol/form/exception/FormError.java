/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.exception;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:28
 */
public class FormError implements Serializable {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String      errorCode;

    private final String      description;

    private final String      uri;

    /**
     * Constructs an {@code FormError} using the provided parameters.
     * @param errorCode the error code
     */
    public FormError(String errorCode) {
        this(errorCode, null, null);
    }

    /**
     * Constructs an {@code FormError} using the provided parameters.
     * @param errorCode the error code
     * @param description the error description
     * @param uri the error uri
     */
    public FormError(String errorCode, String description, String uri) {
        Assert.hasText(errorCode, "errorCode cannot be empty");
        this.errorCode = errorCode;
        this.description = description;
        this.uri = uri;
    }

    /**
     * Returns the error code.
     * @return the error code
     */
    public final String getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the error description.
     * @return the error description
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * Returns the error uri.
     * @return the error uri
     */
    public final String getUri() {
        return this.uri;
    }

    @Override
    public String toString() {
        return "[" + this.getErrorCode() + "] "
               + ((this.getDescription() != null) ? this.getDescription() : "");
    }

}
