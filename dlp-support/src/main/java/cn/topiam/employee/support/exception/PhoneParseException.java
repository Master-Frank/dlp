package cn.topiam.employee.support.exception;

import org.springframework.http.HttpStatus;

/**
 * 手机号解析异常
 * 当手机号解析失败时抛出此异常
 */
public class PhoneParseException extends TopIamException {
    /**
     * 构造函数
     */
    public PhoneParseException() {
        super(
            "手机号格式错误", 
            HttpStatus.BAD_REQUEST
        );
    }
}