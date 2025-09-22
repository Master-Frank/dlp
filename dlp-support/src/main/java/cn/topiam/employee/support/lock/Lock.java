package cn.topiam.employee.support.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 * 用于方法级别的分布式锁控制
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    /**
     * 时间单位
     *
     * @return 时间单位，默认为秒
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 锁定时间
     *
     * @return 锁定时间，默认为1800秒
     */
    long time() default 1800L;

    /**
     * 是否抛出异常
     *
     * @return 当获取锁失败时是否抛出异常，默认为true
     */
    boolean throwException() default true;

    /**
     * 锁键
     *
     * @return 锁键表达式，默认为空
     */
    String key() default "";

    /**
     * 命名空间
     *
     * @return 命名空间，默认为空
     */
    String namespaces() default "";
}