/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 应用程序启动入口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/9
 */
@ServletComponentScan
@SpringBootApplication
public class EiamConsoleApplication {
    public static void main(String[] args) {
        SpringApplication.run(EiamConsoleApplication.class, args);
    }
}
