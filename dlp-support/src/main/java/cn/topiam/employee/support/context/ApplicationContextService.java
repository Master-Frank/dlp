package cn.topiam.employee.support.context;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;

/**
 * Application context service class
 * Provides access and operation functions for Spring application context
 */
public final class ApplicationContextService {
    /**
     * Application context instance
     */
    private static ApplicationContext applicationContext;

    /**
     * Refresh all configurations
     * 
     * @deprecated
     */
    @Deprecated
    private static void refreshAll() {
        refresh("__REFRESH_ALL__");
    }

    /**
     * Get Bean instance by type
     *
     * @param clazz Bean type
     * @param <T> Bean type parameter
     * @return Bean instance
     */
    public static <T> T getBean(Class<T> clazz) {
        return (T) getApplicationContext().getBean(clazz);
    }

    /**
     * Get Bean provider by resolvable type
     *
     * @param type Resolvable type
     * @param <T> Bean type parameter
     * @return Bean provider
     */
    public static <T> ObjectProvider<T> getBeanProvider(ResolvableType type) {
        return getApplicationContext().getBeanProvider(type);
    }

    /**
     * Get Bean instance by name and type
     *
     * @param name Bean name
     * @param clazz Bean type
     * @param <T> Bean type parameter
     * @return Bean instance
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return (T) getApplicationContext().getBean(name, clazz);
    }

    /**
     * Refresh specified configuration
     *
     * @param config Configuration name
     */
    public static void refresh(String config) {
        ((RedissonClient) getBean(RedissonClient.class)).getTopic("eiam-config-refresh").publish(config);
    }

    /**
     * Decrypt string
     *
     * @param str String to be decrypted
     * @return Decrypted string
     */
    public static String decryptString(Object str) {
        int key1 = 3 << 3 ^ 2 ^ 5;
        int key2 = 4 << 4 ^ (2 ^ 5) << (5 >> 2);
        int key3 = 5 << 4 ^ 2 << 2 ^ 4 ^ 5;
        String s = (String) str;
        int length = s.length();
        char[] chars = new char[length];
        int index = length - (2 ^ 3);

        for (int i = key2; index >= 0; index--) {
            chars[index] = (char) (s.charAt(index) ^ key1);
            if (index < 0) {
                break;
            }
            index--;
            chars[index] = (char) (s.charAt(index) ^ key3);
        }

        return new String(chars);
    }

    /**
     * Get application context
     *
     * @return Application context instance
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Set application context
     *
     * @param applicationContext Application context instance
     */
    public static void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        ApplicationContextService.applicationContext = applicationContext;
    }

    /**
     * Get Bean instance by name
     *
     * @param name Bean name
     * @return Bean instance
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
}