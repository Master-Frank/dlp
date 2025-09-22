package cn.topiam.employee.support.util;

/**
 * 脱敏工具类
 * 提供各种数据脱敏的工具方法
 */
public class DesensitizationUtils {
   
   /**
    * 字符串解密方法
    * 
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
   public static String decryptString(Object object) {
      int key1 = (2 ^ 5) << 3 ^ 2 ^ 5;
      int key2 = 5 << 4 ^ 2 << 2 ^ 2 ^ 3;
      int key3 = (3 ^ 5) << 3 ^ 2;
      String str = (String) object;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (3 >> 1);
      char[] result = chars;
      int var4 = key3;
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
    * 身份证护照脱敏
    *
    * @param str 待脱敏字符串
    * @param length 保留长度
    * @return 脱敏后的字符串
    */
   public static String idPassport(String str, int length) {
      int strLength;
      return org.apache.commons.lang3.StringUtils.isBlank(str) ? "" : org.apache.commons.lang3.StringUtils.rightPad(org.apache.commons.lang3.StringUtils.left(str, (strLength = org.apache.commons.lang3.StringUtils.length(str)) - length), strLength, "*");
   }

   /**
    * 手机号加密
    *
    * @param phone 手机号
    * @return 加密后的手机号
    */
   public static String phoneEncrypt(String phone) {
      if (!org.apache.commons.lang3.StringUtils.isEmpty(phone)) {
         if (phone.length() == 11) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
         }
      }

      return phone;
   }

   /**
    * 前后保留脱敏
    *
    * @param str 待脱敏字符串
    * @param leftLength 左侧保留长度
    * @param rightLength 右侧保留长度
    * @return 脱敏后的字符串
    */
   public static String around(String str, int leftLength, int rightLength) {
      return org.apache.commons.lang3.StringUtils.isBlank(str) ? "" : org.apache.commons.lang3.StringUtils.left(str, leftLength).concat(org.apache.commons.lang3.StringUtils.removeStart(org.apache.commons.lang3.StringUtils.leftPad(org.apache.commons.lang3.StringUtils.right(str, rightLength), org.apache.commons.lang3.StringUtils.length(str), "*"), "*"));
   }

   /**
    * 右侧脱敏
    *
    * @param str 待脱敏字符串
    * @param length 保留长度
    * @return 脱敏后的字符串
    */
   public static String right(String str, int length) {
      return org.apache.commons.lang3.StringUtils.isBlank(str) ? "" : org.apache.commons.lang3.StringUtils.leftPad(org.apache.commons.lang3.StringUtils.right(str, length), org.apache.commons.lang3.StringUtils.length(str), "*");
   }

   /**
    * 邮箱加密
    *
    * @param email 邮箱
    * @return 加密后的邮箱
    */
   public static String emailEncrypt(String email) {
      return org.apache.commons.lang3.StringUtils.isBlank(email) ? "" : email.replaceAll("(^\\w{1})[^@]*(@\\w+)", "$1****$2");
   }

   /**
    * 身份证加密
    *
    * @param id 身份证号
    * @return 加密后的身份证号
    */
   public static String idEncrypt(String id) {
      if (!org.apache.commons.lang3.StringUtils.isEmpty(id)) {
         if (id.length() >= 8) {
            return id.replaceAll("(\\d{4})\\d*(\\w{4})", "$1****$2");
         }
      }

      return id;
   }

   /**
    * 左侧脱敏
    *
    * @param str 待脱敏字符串
    * @param length 保留长度
    * @return 脱敏后的字符串
    */
   public static String left(String str, int length) {
      return org.apache.commons.lang3.StringUtils.isBlank(str) ? "" : org.apache.commons.lang3.StringUtils.rightPad(org.apache.commons.lang3.StringUtils.left(str, length), org.apache.commons.lang3.StringUtils.length(str), "*");
   }

   /**
    * 身份证护照脱敏
    *
    * @param str 待脱敏字符串
    * @return 脱敏后的字符串
    */
   public static String idPassport(String str) {
      if (!org.apache.commons.lang3.StringUtils.isEmpty(str)) {
         if (str.length() >= 8) {
            String start = str.substring(0, 2);
            char[] chars = new char[str.length() - 5];
            String middle = (new String(chars)).replace("\u0007", "*");
            return start + middle + str.substring(str.length() - 3);
         }
      }

      return str;
   }
}