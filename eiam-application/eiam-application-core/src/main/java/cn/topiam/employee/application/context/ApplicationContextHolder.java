/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.context;

/**
 * ApplicationContextHolder
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/29 22:37
 */
public final class ApplicationContextHolder {
    private static final ThreadLocal<ApplicationContext> HOLDER = new ThreadLocal<>();

    private ApplicationContextHolder() {
    }

    /**
     * Returns the {@link ApplicationContext} bound to the current thread.
     *
     * @return the {@link ApplicationContext}
     */
    public static ApplicationContext getApplicationContext() {
        return HOLDER.get();
    }

    /**
     * Bind the given {@link ApplicationContext} to the current thread.
     *
     * @param applicationContext the {@link ApplicationContext}
     */
    public static void setContext(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            resetContext();
        } else {
            HOLDER.set(applicationContext);
        }
    }

    /**
     * Reset the {@link ApplicationContext} bound to the current thread.
     */
    public static void resetContext() {
        HOLDER.remove();
    }

}
