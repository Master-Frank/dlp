package cn.topiam.employee.support.geo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 地理位置提供者类
 * 表示地理位置信息的提供者
 */
public class GeoLocationProvider implements Serializable {
    
    /**
     * 提供者标识
     */
    private String provider;
    
    /**
     * 提供者名称
     */
    private String name;
    
    /**
     * 无提供者的默认实例
     */
    public static GeoLocationProvider NONE = new GeoLocationProvider("none", "None");

    /**
     * 默认构造函数
     */
    public GeoLocationProvider() {
    }

    /**
     * 构造函数
     *
     * @param provider 提供者标识
     * @param name 提供者名称
     */
    public GeoLocationProvider(String provider, String name) {
        this.provider = provider;
        this.name = name;
    }

    /**
     * 获取提供者标识
     *
     * @return 提供者标识
     */
    public String getProvider() {
        return this.provider;
    }

    /**
     * 设置提供者标识
     *
     * @param provider 提供者标识
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * 获取提供者名称
     *
     * @return 提供者名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置提供者名称
     *
     * @param name 提供者名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 生成哈希码
     *
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return (new HashCodeBuilder(17, 37)).append(this.provider).append(this.name).toHashCode();
    }

    /**
     * 判断对象是否相等
     *
     * @param obj 比较对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            GeoLocationProvider that = (GeoLocationProvider) obj;
            return (new EqualsBuilder()).append(this.provider, that.provider).append(this.name, that.name).isEquals();
        } else {
            return false;
        }
    }
}
