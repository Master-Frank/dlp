package cn.topiam.employee.support.jackjson;

import cn.topiam.employee.support.geo.jackson.GeoLocationJacksonModule;
import cn.topiam.employee.support.security.jackjson.SecurityJacksonModule;
import cn.topiam.employee.support.web.jackjson.WebJacksonModule;
import com.fasterxml.jackson.databind.Module;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * 支持Jackson2的模块配置类
 * 提供对GeoLocation、Security和Web相关Jackson模块的支持
 */
public class SupportJackson2Module {
    
    /**
     * 解密方法
     *
     * @param object 需要解密的对象
     * @return 解密后的字符串
     */
    public static String decryptString(Object object) {
        int var1 = 4 << 4 ^ 3;
        int var2 = 3 << 3;
        int var3 = (3 ^ 5) << 4 ^ 3 << 2 ^ 3;
        String str = (String) object;
        int length = str.length();
        char[] chars = new char[length];
        int index = length - (5 >> 2);
        char[] result = chars;
        int var4 = var3;
        int var5 = index;

        for(int var6 = var2; var5 >= 0; var5 = index) {
            int tempIndex = index;
            int charValue = str.charAt(index);
            --index;
            result[tempIndex] = (char)(charValue ^ var6);
            if (index < 0) {
                break;
            }

            int tempIndex2 = index--;
            result[tempIndex2] = (char)(str.charAt(tempIndex2) ^ var4);
        }

        return new String(result);
    }

    /**
     * 获取模块列表
     *
     * @param classLoader 类加载器
     * @return 模块列表
     */
    public static List<Module> getModules(ClassLoader classLoader) {
        List<Module> modules = new ArrayList<>(SecurityJackson2Modules.getModules(classLoader));
        modules.add(new GeoLocationJacksonModule());
        modules.add(new SecurityJacksonModule());
        modules.add(new WebJacksonModule());
        return modules;
    }
}