package cn.topiam.employee.support.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;

/**
 * 地理位置信息类
 * 包含IP地址对应的地理位置信息
 */
public class GeoLocation implements Serializable {
    private static final long serialVersionUID = 991484919483509517L;
    
    /**
     * IP地址
     */
    private String ip;
    
    /**
     * 大洲代码
     */
    private String continentCode;
    
    /**
     * 大洲名称
     */
    private String continentName;
    
    /**
     * 国家代码
     */
    private String countryCode;
    
    /**
     * 国家名称
     */
    private String countryName;
    
    /**
     * 城市代码
     */
    private String cityCode;
    
    /**
     * 城市名称
     */
    private String cityName;
    
    /**
     * 省份代码
     */
    private String provinceCode;
    
    /**
     * 省份名称
     */
    private String provinceName;
    
    /**
     * 纬度
     */
    private Double latitude;
    
    /**
     * 经度
     */
    private Double longitude;
    
    /**
     * 地理位置提供者
     */
    private GeoLocationProvider provider;

    /**
     * 构造函数
     */
    public GeoLocation() {
    }

    /**
     * 构造函数
     *
     * @param ip IP地址
     * @param continentCode 大洲代码
     * @param continentName 大洲名称
     * @param countryCode 国家代码
     * @param countryName 国家名称
     * @param cityCode 城市代码
     * @param cityName 城市名称
     * @param provinceCode 省份代码
     * @param provinceName 省份名称
     * @param latitude 纬度
     * @param longitude 经度
     * @param provider 地理位置提供者
     */
    public GeoLocation(String ip, String continentCode, String continentName, String countryCode, 
                       String countryName, String cityCode, String cityName, String provinceCode, 
                       String provinceName, Double latitude, Double longitude, GeoLocationProvider provider) {
        this.ip = ip;
        this.continentCode = continentCode;
        this.continentName = continentName;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
    }

    /**
     * 获取纬度
     *
     * @return 纬度
     */
    @JsonProperty("latitude")
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * 设置大洲名称
     *
     * @param continentName 大洲名称
     */
    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    /**
     * 获取经度
     *
     * @return 经度
     */
    @JsonProperty("longitude")
    public Double getLongitude() {
        return this.longitude;
    }

    /**
     * 获取国家名称
     *
     * @return 国家名称
     */
    @JsonProperty("countryName")
    public String getCountryName() {
        return this.countryName;
    }

    /**
     * 创建GeoLocationBuilder实例
     *
     * @return GeoLocationBuilder实例
     */
    public static GeoLocationBuilder builder() {
        return new GeoLocationBuilder();
    }

    /**
     * 设置IP地址
     *
     * @param ip IP地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取省份名称
     *
     * @return 省份名称
     */
    @JsonProperty("provinceName")
    public String getProvinceName() {
        return this.provinceName;
    }

    /**
     * 设置国家代码
     *
     * @param countryCode 国家代码
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * 设置省份代码
     *
     * @param provinceCode 省份代码
     */
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取国家代码
     *
     * @return 国家代码
     */
    @JsonProperty("countryCode")
    public String getCountryCode() {
        return this.countryCode;
    }

    /**
     * 获取大洲名称
     *
     * @return 大洲名称
     */
    @JsonProperty("continentName")
    public String getContinentName() {
        return this.continentName;
    }

    /**
     * 设置城市名称
     *
     * @param cityName 城市名称
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * 设置省份名称
     *
     * @param provinceName 省份名称
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取城市代码
     *
     * @return 城市代码
     */
    @JsonProperty("cityCode")
    public String getCityCode() {
        return this.cityCode;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 设置国家名称
     *
     * @param countryName 国家名称
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * 设置地理位置提供者
     *
     * @param provider 地理位置提供者
     */
    public void setProvider(GeoLocationProvider provider) {
        this.provider = provider;
    }

    /**
     * 设置大洲代码
     *
     * @param continentCode 大洲代码
     */
    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    /**
     * 获取城市名称
     *
     * @return 城市名称
     */
    @JsonProperty("cityName")
    public String getCityName() {
        return this.cityName;
    }

    /**
     * 获取大洲代码
     *
     * @return 大洲代码
     */
    @JsonProperty("continentCode")
    public String getContinentCode() {
        return this.continentCode;
    }

    /**
     * 获取省份代码
     *
     * @return 省份代码
     */
    @JsonProperty("provinceCode")
    public String getProvinceCode() {
        return this.provinceCode;
    }

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    @JsonProperty("ip")
    public String getIp() {
        return this.ip;
    }

    /**
     * 获取地理位置提供者
     *
     * @return 地理位置提供者
     */
    @JsonProperty("provider")
    public GeoLocationProvider getProvider() {
        return this.provider;
    }

    /**
     * 设置城市代码
     *
     * @param cityCode 城市代码
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 地理位置构建器
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class GeoLocationBuilder {
        private String ip;
        private String continentCode;
        private String continentName;
        private String countryCode;
        private String countryName;
        private String cityCode;
        private String cityName;
        private String provinceCode;
        private String provinceName;
        private Double latitude;
        private Double longitude;
        private GeoLocationProvider provider;

        /**
         * 设置国家名称
         *
         * @param countryName 国家名称
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder countryName(String countryName) {
            this.countryName = countryName;
            return this;
        }

        /**
         * 设置纬度
         *
         * @param latitude 纬度
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        /**
         * 设置IP地址
         *
         * @param ip IP地址
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        /**
         * 设置国家代码
         *
         * @param countryCode 国家代码
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        /**
         * 设置大洲代码
         *
         * @param continentCode 大洲代码
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder continentCode(String continentCode) {
            this.continentCode = continentCode;
            return this;
        }

        /**
         * 设置大洲名称
         *
         * @param continentName 大洲名称
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder continentName(String continentName) {
            this.continentName = continentName;
            return this;
        }

        /**
         * 设置城市代码
         *
         * @param cityCode 城市代码
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder cityCode(String cityCode) {
            this.cityCode = cityCode;
            return this;
        }

        /**
         * 设置经度
         *
         * @param longitude 经度
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        /**
         * 设置地理位置提供者
         *
         * @param provider 地理位置提供者
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder provider(GeoLocationProvider provider) {
            this.provider = provider;
            return this;
        }

        /**
         * 设置城市名称
         *
         * @param cityName 城市名称
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder cityName(String cityName) {
            this.cityName = cityName;
            return this;
        }

        /**
         * 构建GeoLocation实例
         *
         * @return GeoLocation实例
         */
        public GeoLocation build() {
            return new GeoLocation(this.ip, this.continentCode, this.continentName, this.countryCode,
                    this.countryName, this.cityCode, this.cityName, this.provinceCode,
                    this.provinceName, this.latitude, this.longitude, this.provider);
        }

        /**
         * 设置省份代码
         *
         * @param provinceCode 省份代码
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder provinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
            return this;
        }

        /**
         * 设置省份名称
         *
         * @param provinceName 省份名称
         * @return GeoLocationBuilder实例
         */
        public GeoLocationBuilder provinceName(String provinceName) {
            this.provinceName = provinceName;
            return this;
        }
    }
}