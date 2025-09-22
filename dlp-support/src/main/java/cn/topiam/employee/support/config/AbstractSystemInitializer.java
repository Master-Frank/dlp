package cn.topiam.employee.support.config;

/**
 * 抽象系统初始化器
 * 提供系统初始化器的基本实现
 */
public abstract class AbstractSystemInitializer implements SystemInitializer {
    
    /**
     * 获取初始化器的执行顺序
     * 默认返回最小值，确保在其他初始化器之前执行
     * 
     * @return 初始化器执行顺序
     */
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}