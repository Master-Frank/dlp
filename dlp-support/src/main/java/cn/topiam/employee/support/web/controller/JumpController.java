package cn.topiam.employee.support.web.controller;

import cn.topiam.employee.support.security.constant.SecurityConstants;
import cn.topiam.employee.support.security.savedredirect.HttpSessionRedirectCache;
import cn.topiam.employee.support.security.savedredirect.RedirectCache;
import cn.topiam.employee.support.security.util.SecurityUtils;
import cn.topiam.employee.support.util.PhoneUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 跳转控制器
 * 用于处理各种跳转请求
 */
@Controller
public class JumpController {
   
   /**
    * 重定向缓存
    */
   private final RedirectCache redirectCache = new HttpSessionRedirectCache();
   
   /**
    * 保存的请求属性名
    */
   public static final String SAVED_REQUEST = "SAVED_REQUEST";

   /**
    * 退出登录跳转
    * 
    * @param request HTTP请求
    * @param response HTTP响应
    * @throws IOException IO异常
    */
   @GetMapping({"/api/v1/logout"})
   public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
      try {
         request.logout();
      } catch (Exception e) {
         // 忽略异常
      }
      response.setStatus(HttpStatus.OK.value());
   }

   /**
    * 跳转到指定URL
    * 
    * @param request HTTP请求
    * @param code 跳转码
    * @return 重定向视图
    */
   @GetMapping({"/api/v1/jump"})
   public RedirectView jump(HttpServletRequest request, @RequestParam("code") String code) {
      String redirectUrl = (String) request.getSession().getAttribute(code);
      if (Objects.isNull(redirectUrl)) {
         redirectUrl = "/";
      } else {
         request.getSession().removeAttribute(code);
      }

      RedirectView redirectView = new RedirectView();
      redirectView.setUrl(redirectUrl);
      redirectView.setExpandUriTemplateVariables(Boolean.FALSE);
      return redirectView;
   }

   /**
    * 登录成功跳转
    * 
    * @param request HTTP请求
    * @return 重定向视图
    */
   @GetMapping({"/login/success"})
   public RedirectView loginSuccess(HttpServletRequest request) {
      String redirectUrl = (String) request.getSession().getAttribute(SAVED_REQUEST);
      if (Objects.isNull(redirectUrl)) {
         redirectUrl = "/";
      }

      request.getSession().removeAttribute(SAVED_REQUEST);
      RedirectView redirectView = new RedirectView();
      redirectView.setUrl(redirectUrl);
      redirectView.setExpandUriTemplateVariables(Boolean.FALSE);
      return redirectView;
   }
}