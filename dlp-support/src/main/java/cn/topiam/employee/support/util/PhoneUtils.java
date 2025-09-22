package cn.topiam.employee.support.util;

import org.apache.commons.lang3.StringUtils;
import cn.topiam.employee.support.exception.PhoneParseException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * 手机号工具类
 * 提供手机号相关的工具方法
 */
public class PhoneUtils {
   /**
    * 加号标识
    */
   public static final String PLUS_SIGN = "+";
   
   /**
    * 手机号正则表达式
    */
   public static final String PHONE_REGEXP = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16([5,6])|(17[0-8])|(18[0-9]))|(19[1,8,9]))\\d{8}$";
   
   /**
    * 默认国家代码
    */
   public static final String DEFAULT_REGION = "CN";

   /**
    * 获取手机号
    *
    * @param phone 手机号
    * @return 手机号
    */
   public static String getPhoneNumber(String phone) {
      return String.valueOf(parsePhone(phone).getNationalNumber());
   }

   /**
    * 获取手机区号
    *
    * @param phone 手机号
    * @return 手机区号
    */
   public static String getPhoneAreaCode(String phone) {
      return String.valueOf(parsePhone(phone).getCountryCode());
   }

   /**
    * 验证手机号格式是否正确
    *
    * @param phone 手机号
    * @return 手机号格式是否正确
    */
   public static boolean isPhoneValidate(String phone) {
      try {
         if (StringUtils.isNotBlank(phone)) {
            return getPhoneNumber(phone).matches(PHONE_REGEXP);
         }
         return false;
      } catch (PhoneParseException e) {
         return false;
      }
   }

   /**
    * 解析手机号
    *
    * @param phone 手机号
    * @return 手机号对象
    */
   public static Phonenumber.PhoneNumber parsePhone(String phone) {
      return parsePhone(phone, DEFAULT_REGION);
   }
   
   /**
    * 解析手机号
    *
    * @param phone 手机号
    * @param region 国家/地区代码
    * @return 手机号对象
    */
   public static Phonenumber.PhoneNumber parsePhone(String phone, String region) {
      try {
         return PhoneNumberUtil.getInstance().parse(phone, region);
      } catch (NumberParseException e) {
         throw new PhoneParseException();
      }
   }

   /**
    * 字符串解密方法
    * 
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
   public static String decryptString(Object object) {
      // 输入验证
      if (object == null) {
         return null;
      }
      
      String str = object.toString();
      if (str.isEmpty()) {
         return str;
      }
      
      // 解密密钥
      int key1 = 2 << 3 ^ 2;
      int key2 = 4 << 4 ^ (4 ^ 5) << (4 ^ 5);
      int key3 = 5 << 4;
      
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (4 ^ 5);
      char[] result = chars;
      int secondKey = key2;
      int currentIndex = index;

      for (int firstKey = key1; currentIndex >= 0; currentIndex = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char) (charValue ^ firstKey);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ secondKey);
      }

      return new String(result);
   }
}