/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ForwardController
 *
 * @author TopIAM
 * Created by support@topiam.cn on  2022/12/4 22:16
 */
@Controller
public class FrontendForwardController {

    /**
     * forward
     *
     * @return forward to client {@code index.html}.
     */
    @GetMapping(value = "/")
    public String forward() {
        return "forward:index.html";
    }
}