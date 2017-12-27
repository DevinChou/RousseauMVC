package org.rousseau4j.framework.helper;

import org.rousseau4j.framework.util.ReflectionUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean工厂
 * Created by ZhouHangqi on 2017/7/26.
 */
public final class BeanHelper {

    /**
     * Bean映射
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new ConcurrentHashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet) {
            BEAN_MAP.put(beanClass, ReflectionUtil.newInstance(beanClass));
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls) {
        Object res = BEAN_MAP.get(cls);
        if (res == null) {
            throw new RuntimeException("cannot get bean by class: " + cls);
        }
        return (T)res;
    }

    public static void setBean(Class<?> cls, Object object) {
        BEAN_MAP.put(cls, object);
    }
}
