package cn.topiam.employee.support.exception;

/**
 * 认证异常
 * 当认证过程中发生错误时抛出此异常
 */
public class AuthenticationException extends TopIamException {
    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}