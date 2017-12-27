package org.rousseau4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.rousseau4j.framework.annotation.Resource;
import org.rousseau4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 简单的单例依赖注入
 * Created by ZhouHangqi on 2017/7/27.
 */
public final class IocHelper {

    /**
     * 遍历所有bean,遍历每个bean的field,对于使用Resource注解的field,进行注入
     */
    static  {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (!beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanObject = beanEntry.getValue();
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)) {
                    for (Field beanField : beanFields) {
                        if (beanField.isAnnotationPresent(Resource.class)) {
                            Class<?> beanFieldClass = beanField.getClass();
                            Object beanFieldObject = beanMap.get(beanFieldClass);
                            if (beanFieldObject != null) {
                                ReflectionUtil.setField(beanObject, beanField, beanFieldObject);
                            }
                        }
                    }
                }
            }
        }
    }
}
