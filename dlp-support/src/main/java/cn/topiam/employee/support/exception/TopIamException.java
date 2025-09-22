package cn.topiam.employee.support.exception;

import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.util.EmailUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.http.HttpStatus;

/**
 * TopIAM异常基类
 * 所有TopIAM相关异常的基类
 */
public class TopIamException extends RuntimeException {
    public static final String BIND_EXCEPTION = "bind_exception";
    public static final String DESCRIPTION = "error_description";
    public static final String STATUS = "status";
    public static final String DEFAULT_EXCEPTION = "unknown_exception";
    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "constraint_violation_exception";
    public static final String ERROR = "error";
    public static final HttpStatus DEFAULT_STATUS;
    
    /**
     * 错误码
     */
    private final String errorCode;
    
    /**
     * 附加信息
     */
    private Map<String, String> additionalInformation;
    
    /**
     * HTTP状态码
     */
    private final HttpStatus httpStatus;

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public TopIamException(String message) {
        this("unknown_exception", message, DEFAULT_STATUS);
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public TopIamException(String message, HttpStatus httpStatus) {
        this("unknown_exception", message, httpStatus);
    }

    static {
        DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 获取附加信息
     *
     * @return 附加信息映射
     */
    public Map<String, String> getAdditionalInformation() {
        return this.additionalInformation;
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public TopIamException(Throwable cause, String errorCode, String message, HttpStatus httpStatus) {
        super(message, cause);
        this.additionalInformation = null;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public TopIamException(String message, Throwable cause) {
        super(message, cause);
        this.additionalInformation = null;
        this.errorCode = "unknown_exception";
        this.httpStatus = DEFAULT_STATUS;
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param message 异常消息
     */
    public TopIamException(String errorCode, String message) {
        super(message);
        this.additionalInformation = null;
        this.errorCode = errorCode;
        this.httpStatus = DEFAULT_STATUS;
    }

    /**
     * 获取HTTP状态码
     *
     * @return HTTP状态码
     */
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public TopIamException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.additionalInformation = null;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * 根据映射创建异常实例
     *
     * @param exceptionMap 异常信息映射
     * @return TopIamException实例
     */
    public static TopIamException valueOf(Map<String, String> exceptionMap) {
        String errorCode = exceptionMap.get("error");
        String errorMessage = (String) exceptionMap.getOrDefault("error_description", null);
        HttpStatus status = DEFAULT_STATUS;
        if (exceptionMap.containsKey("status")) {
            try {
                status = HttpStatus.valueOf(Integer.parseInt(exceptionMap.get("status")));
            } catch (NumberFormatException ignored) {
            }
        }

        TopIamException exception = new TopIamException(errorCode, errorMessage, status);
        Iterator<Map.Entry<String, String>> iterator = exceptionMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            if (!"error".equals(key) && !"error_description".equals(key)) {
                exception.addAdditionalInformation(key, entry.getValue());
            }
        }

        return exception;
    }

    /**
     * 获取异常摘要信息
     *
     * @return 异常摘要信息
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        String value;
        
        if ((value = this.getErrorCode()) != null) {
            sb.append(delimiter).append("error: ").append(value).append("\n");
            delimiter = "error: ";
        }

        if ((value = this.getMessage()) != null) {
            sb.append(delimiter).append("error_description: ").append(value).append("\n");
            delimiter = "error_description: \n";
        }

        Map<String, String> additionalInfo;
        if ((additionalInfo = this.getAdditionalInformation()) != null) {
            for (Map.Entry<String, String> entry : additionalInfo.entrySet()) {
                sb.append(delimiter).append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                delimiter = "error: ";
            }
        }

        return sb.toString();
    }

    /**
     * 添加附加信息
     *
     * @param key 键
     * @param value 值
     */
    public void addAdditionalInformation(String key, String value) {
        if (this.additionalInformation == null) {
            this.additionalInformation = new TreeMap<>();
        }

        this.additionalInformation.put(key, value);
    }

    @Override
    public String toString() {
        return this.getSummary();
    }
}