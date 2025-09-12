/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.endpoint;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/19 22:20
 */
public class FormParameterNames {

    /**
     * {@code error} - used in Authorization Response and Access Token Response.
     */
    public static final String ERROR             = "error";

    /**
     * {@code error_description} - used in Authorization Response and Access Token
     * Response.
     */
    public static final String ERROR_DESCRIPTION = "error_description";

    /**
     * {@code error_uri} - used in Authorization Response and Access Token Response.
     */
    public static final String ERROR_URI         = "error_uri";

    private FormParameterNames() {
    }
}
