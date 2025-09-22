package cn.topiam.employee.support.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Favicon控制器
 * 用于处理网站图标请求
 */
@Controller
public class FaviconController {
   
   /**
    * Favicon资源路径
    */
   private static final String FAVICON_PATH = "static/favicon.ico";

   /**
    * 获取Favicon图标
    * 
    * @param request HTTP请求
    * @return Favicon图标响应
    */
   @GetMapping(
      value = {"/favicon.ico"},
      produces = {MediaType.IMAGE_X_ICON_VALUE}
   )
   @ResponseBody
   public ResponseEntity<Resource> favicon(HttpServletRequest request) {
      ClassPathResource favicon = new ClassPathResource("static/favicon.ico");
      return ResponseEntity.ok().body(favicon);
   }
}