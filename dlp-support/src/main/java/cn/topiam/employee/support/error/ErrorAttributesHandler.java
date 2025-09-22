package cn.topiam.employee.support.error;

import java.util.Map;

/**
 * 错误属性处理器接口
 * 定义处理错误属性的规范
 */
public interface ErrorAttributesHandler {
    /**
     * 获取错误属性
     * 
     * @param throwable 异常对象
     * @return 包含错误信息的Map
     */
    Map<String, Object> getErrorAttributes(Throwable throwable);
}