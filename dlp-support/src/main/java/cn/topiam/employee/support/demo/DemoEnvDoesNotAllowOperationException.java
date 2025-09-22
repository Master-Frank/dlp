package cn.topiam.employee.support.demo;

import cn.topiam.employee.support.exception.TopIamException;
import org.springframework.http.HttpStatus;

/**
 * 演示环境不允许操作异常
 * 在演示环境中执行不允许的操作时抛出此异常
 */
public class DemoEnvDoesNotAllowOperationException extends TopIamException {
    /**
     * 构造函数
     */
    public DemoEnvDoesNotAllowOperationException() {
        super(
            "演示环境不允许此操作", 
            HttpStatus.FORBIDDEN
        );
    }
}