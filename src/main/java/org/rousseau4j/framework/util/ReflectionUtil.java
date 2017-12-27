package org.rousseau4j.framework.util;

import lombok.extern.log4j.Log4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ZhouHangqi on 2017/7/26.
 */
@Log4j
public final class ReflectionUtil {

    /**
     * 创建实例
     * @param cls
     * @return
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        }  catch (Exception e) {
            log.error("class instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     * @param object
     * @param method
     * @param args
     * @return
     */
    public static Object invokeMethod(Object object, Method method, Object...args) {
        Object result;
        try {
            result = method.invoke(object, args);
        } catch (Exception e) {
            log.error("invoke method failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置域的值
     * @param object
     * @param field
     * @param value
     */
    public static void setField(Object object, Field field, Object value) {
        try {
            field.set(object, value);
        } catch (Exception e) {
            log.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }
}
