package cn.topiam.employee.support.exception;

/**
 * 模板不存在异常
 * <p>
 * 当请求的模板在系统中不存在时抛出此异常，用于标识资源未找到的情况。
 * </p>
 */
public class TemplateNotExistException extends TopIamException {

    /**
     * 构造函数
     *
     * @param cause 导致模板不存在的具体异常原因
     */
    public TemplateNotExistException(Throwable cause) {
        super("模板不存在", cause);
    }
}