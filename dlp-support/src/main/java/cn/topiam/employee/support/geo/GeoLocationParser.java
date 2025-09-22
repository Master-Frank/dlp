package cn.topiam.employee.support.geo;

/**
 * 地理位置解析器接口
 * 定义根据IP地址获取地理位置信息的方法
 */
public interface GeoLocationParser {
    
    /**
     * 根据IP地址获取地理位置信息
     *
     * @param ip IP地址
     * @return 地理位置信息
     */
    GeoLocation getGeoLocation(String ip);
}