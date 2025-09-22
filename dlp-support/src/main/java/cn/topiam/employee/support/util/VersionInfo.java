package cn.topiam.employee.support.util;

import cn.topiam.employee.support.util.PhoneUtils;

/**
 * 版本信息类
 * 提供系统版本信息的工具类
 */
public class VersionInfo {
   
   /**
    * 序列化版本UID
    */
   public static final long SERIAL_VERSION_UID = (long) getVersion().hashCode();
   
   // 合成字段
   private static final int D = 0;
   
   // 合成字段
   private static final int b = 0;
   
   // 合成字段
   private static final int SYNTHETIC_FIELD = 2;

    /**
     * 获取版本信息
     * 
     * @return 版本字符串
     */
    public static String getVersion() {
        return PhoneUtils.decryptString("p<r<r?\u0001W");
    }
}