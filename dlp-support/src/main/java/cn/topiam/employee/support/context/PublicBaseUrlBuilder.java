package cn.topiam.employee.support.context;

import org.springframework.util.Assert;
import cn.topiam.employee.support.util.PhoneUtils;
import cn.topiam.employee.support.util.VersionUtils;
import lombok.Data;
import java.io.Serializable;

/**
 * Public base URL builder
 *
 * @author TopIAM
 * Created by support on 2020/8/18 21:25
 */
@Data
public class PublicBaseUrlBuilder implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8043605311513960464L;
    
    /**
     * Protocol scheme (http/https)
     */
    private String scheme;
    
    /**
     * Server name
     */
    private String serverName;
    
    /**
     * Port number
     */
    private Integer port;
    
    /**
     * Context path
     */
    private String contextPath;


    /**
     * Build URL
     *
     * @return {@link String}
     */
    public String build() {
        PublicBaseUrlBuilder builder = this;
        Assert.notNull(builder.scheme, "scheme不能为空");
        Assert.notNull(builder.serverName, "serverName不能为空");
        StringBuilder sb = new StringBuilder();
        sb.append(builder.scheme).append("://").append(builder.serverName);
        if (builder.port != null && !"http".equals(builder.scheme) && !"https".equals(builder.scheme)) {
            sb.append(":").append(builder.port);
        }
        if (builder.contextPath != null) {
            sb.append(builder.contextPath);
        }
        return sb.toString();
    }

    public PublicBaseUrlBuilder setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public PublicBaseUrlBuilder setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public PublicBaseUrlBuilder setPort(Integer port) {
        this.port = port;
        return this;
    }

    public PublicBaseUrlBuilder setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }
}