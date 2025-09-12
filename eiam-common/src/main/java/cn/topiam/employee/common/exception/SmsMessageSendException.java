/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 短信消息发送失败
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/26 18:01
 */
public class SmsMessageSendException extends TopIamException {
    public SmsMessageSendException() {
        super("message_send_error", "发送短信消息失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public SmsMessageSendException(Throwable cause) {
        super(cause, "message_send_error", "发送短信消息失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SmsMessageSendException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public SmsMessageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
