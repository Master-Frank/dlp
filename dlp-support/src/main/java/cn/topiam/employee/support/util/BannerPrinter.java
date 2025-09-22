package cn.topiam.employee.support.util;

import cn.topiam.employee.support.util.PhoneUtils;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

/**
 * 横幅打印器
 * 用于打印应用启动时的横幅信息
 */
public class BannerPrinter implements Banner {
   
   /**
    * 打印横幅
    * 
    * @param environment 环境信息
    * @param sourceClass 源类
    * @param out 输出对象
    */
   public void printBanner(Environment environment, Class<?> sourceClass, java.io.PrintStream out) {
      out.println(PhoneUtils.decryptString("\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
   }
}