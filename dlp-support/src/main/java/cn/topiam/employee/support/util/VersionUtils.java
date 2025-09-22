package cn.topiam.employee.support.util;

import cn.topiam.employee.support.Version;
import java.util.Objects;

/**
 * 版本工具类
 * 提供版本相关的工具方法
 */
public final class VersionUtils {
   
   /**
    * 字符串解密方法
    * 
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
   public static String decryptString(Object object) {
      int key1 = 5 << 4;
      int key2 = 5 << 3;
      int key3 = (3 ^ 5) << 4 ^ 2 ^ 5;
      String str = (String) object;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (5 >> 2);
      char[] result = chars;
      int var4 = key2;
      int var5 = index;

      for (int var6 = key1; var5 >= 0; var5 = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char) (charValue ^ var6);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ var4);
      }

      return new String(result);
   }

   /**
    * 私有构造函数，防止实例化
    */
   private VersionUtils() {
   }

   /**
    * 获取版本号
    *
    * @return 版本号
    */
   public static String getVersion() {
      return Version.getVersion();
   }

   /**
    * 获取类的版本号
    *
    * @param clazz 类
    * @return 版本号
    */
   public static String getVersion(Class<?> clazz) {
      Package pkg;
      return !Objects.isNull(pkg = clazz.getPackage()) ? Objects.toString(pkg.getImplementationVersion(), Version.getVersion()) : Version.getVersion();
   }
}