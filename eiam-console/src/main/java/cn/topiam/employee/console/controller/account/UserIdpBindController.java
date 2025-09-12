/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.controller.account;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.topiam.employee.common.constant.AccountConstants.USER_PATH;

/**
 * 用户身份提供商绑定
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:06
 */
@Validated
@Tag(name = "用户身份提供商绑定关系")
@RestController
@AllArgsConstructor
@RequestMapping(value = USER_PATH + "/idp", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserIdpBindController {

}
