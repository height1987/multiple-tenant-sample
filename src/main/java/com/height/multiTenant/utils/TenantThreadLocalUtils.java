package com.height.multiTenant.utils;

public class TenantThreadLocalUtils {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "";
        }
    };
    public static void setContextStr(String tenantContext) {
        threadLocal.set(tenantContext);
    }
    public static void clearContext(){
        threadLocal.remove();
    }

    public static String getContextStr() {
        return threadLocal.get();
    }

    public static TenantContext getContext() {
        return TenantContext.parse(getContextStr());
    }
}
