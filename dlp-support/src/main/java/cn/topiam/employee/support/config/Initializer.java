package cn.topiam.employee.support.config;

import org.springframework.core.Ordered;

/**
 * 初始化器接口
 * 定义系统初始化器的基本行为
 */
public interface Initializer extends Ordered {
    
    /**
     * 执行初始化操作
     * 
     * @throws InitializationException 初始化异常
     */
    void init() throws InitializationException;
}