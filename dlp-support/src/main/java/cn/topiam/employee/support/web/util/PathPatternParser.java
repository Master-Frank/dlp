package cn.topiam.employee.support.web.util;

import cn.topiam.employee.support.context.ApplicationContextService;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路径模式解析器类
 * 扩展Spring的PathPatternParser，提供额外的路径参数提取功能
 */
public class PathPatternParser extends org.springframework.web.util.pattern.PathPatternParser {
   
   /**
    * 提取路径中的参数名
    *
    * @param path 路径
    * @return 参数名列表
    */
   public static List<String> extractParameterNames(String path) {
      String pattern = path;
      List<String> parameterNames = new ArrayList<>();
      Matcher matcher = Pattern.compile(ApplicationContextService.decryptString("\u00055q\u0015\u0007a\u0004efg\u00053")).matcher(pattern);

      while (matcher.find()) {
         String parameterName = matcher.group(1);
         parameterNames.add(parameterName);
      }

      return parameterNames;
   }
}