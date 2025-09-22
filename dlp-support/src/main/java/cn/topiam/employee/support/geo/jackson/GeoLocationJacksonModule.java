package cn.topiam.employee.support.geo.jackson;

import cn.topiam.employee.support.Fa;
import cn.topiam.employee.support.da;
import cn.topiam.employee.support.geo.GeoLocation;
import cn.topiam.employee.support.geo.GeoLocationProvider;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

/**
 * 地理位置相关的Jackson模块
 * 用于配置GeoLocation和GeoLocationProvider的Jackson序列化/反序列化
 */
public class GeoLocationJacksonModule extends Module {
    
    /**
     * 获取模块名称
     *
     * @return 模块名称
     */
    @Override
    public String getModuleName() {
        return GeoLocationJacksonModule.class.getName();
    }

    /**
     * 设置模块配置
     *
     * @param context 配置上下文
     */
    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(GeoLocation.class, da.class);
        context.setMixInAnnotations(GeoLocationProvider.class, Fa.class);
    }

    /**
     * 获取模块版本
     *
     * @return 模块版本
     */
    @Override
    public Version version() {
        return new Version(1, 6, 0, null, null, null);
    }
}