package cn.topiam.employee.support.enums;

/**
 * 枚举基础接口
 * 定义枚举类型的基本方法
 */
public interface BaseEnum {
    /**
     * 获取枚举编码
     * 
     * @return 枚举编码
     */
    String getCode();

    /**
     * 获取枚举描述
     * 
     * @return 枚举描述
     */
    String getDesc();
}