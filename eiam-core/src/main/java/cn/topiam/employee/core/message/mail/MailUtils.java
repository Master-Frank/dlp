/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.message.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.web.util.HtmlUtils;

import cn.topiam.employee.common.exception.MailTemplateException;

/**
 * 邮件工具类
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/4 21:30
 */
public class MailUtils {
    /**
     * 获取内容文件
     *
     * @param resourceLocation {@link File}
     * @return {@link File}
     */
    public static String readEmailContent(@NonNull String resourceLocation) {
        try {
            ClassPathResource resource = new ClassPathResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            content = StringUtils.remove(content,
                StringUtils.substringBetween(content, "<!--", "-->"));
            content = StringUtils.remove(StringUtils.remove(content, "<!--"), "-->");
            content = HtmlUtils.htmlEscape(content);
            return content;
        } catch (IOException e) {
            throw new MailTemplateException("获取文件模板失败", e);
        }
    }

}
