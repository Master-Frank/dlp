package cn.topiam.employee.support.exception;

import org.springframework.http.HttpStatus;

/**
 * 信息有效性验证失败异常
 * 当信息有效性验证失败时抛出此异常
 */
public class InfoValidityFailException extends TopIamException {
    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public InfoValidityFailException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public InfoValidityFailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public InfoValidityFailException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public InfoValidityFailException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public InfoValidityFailException(Throwable cause, String errorCode, String message, HttpStatus httpStatus) {
        super(cause, errorCode, message, httpStatus);
    }
}