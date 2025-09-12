/*
 * eiam-synchronizer - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.synchronizer.configuration;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *  IdentitySourceBeanUtils
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/21 23:22
 */
public class IdentitySourceBeanUtils {

    /**
     * 获取Bean名称
     *
     * @param id {@link String}
     * @return {@link  String}
     */
    public static String getSourceBeanName(String id) {
        return "identitySourceBean_" + DigestUtils.md5Hex(id);
    }
}
