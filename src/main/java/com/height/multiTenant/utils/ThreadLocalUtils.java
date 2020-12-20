package com.height.multiTenant.utils;

public class ThreadLocalUtils {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "";
        }
    };
    public static void setContextStr(String parkContext) {
        threadLocal.set(parkContext);
    }

    public static String getContextStr() {
        return threadLocal.get();
    }

    public static ParkContext getContext() {
        return ParkContext.parse(getContextStr());
    }
}
