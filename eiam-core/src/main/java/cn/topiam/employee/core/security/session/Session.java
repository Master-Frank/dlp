/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.security.session;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cn.topiam.employee.support.constant.EiamConstants;
import cn.topiam.employee.support.geo.GeoLocation;
import cn.topiam.employee.support.security.userdetails.UserType;
import cn.topiam.employee.support.web.useragent.UserAgent;

import lombok.Data;

/**
 *SessionDetails
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/13 21:25
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Session implements Serializable {

    @Serial
    private static final long serialVersionUID = 8850489178248613501L;

    /**
     * 会话ID
     */
    private String            sessionId;

    /**
     * 用户ID
     */
    private final String      id;

    /**
     * 用户名
     */
    private final String      username;

    /**
     * 地址位置相关
     */
    private GeoLocation       geoLocation;

    /**
     * userAgent
     */
    private UserAgent         userAgent;

    /**
     * 用户类型
     */
    private UserType          userType;

    /**
     * 认证类型
     */
    private String            authenticationProvider;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime     authenticationTime;

    /**
     * 最后请求时间
     */
    @JsonFormat(pattern = EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime     lastRequestTime;

}
