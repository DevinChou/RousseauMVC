package org.rousseau4j.framework.helper;

import org.rousseau4j.framework.annotation.Controller;
import org.rousseau4j.framework.annotation.Service;
import org.rousseau4j.framework.util.ClassLoadUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手
 * Created by ZhouHangqi on 2017/7/26.
 */
public final class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String baskPackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassLoadUtil.getClassSet(baskPackage);
    }

    /**
     * 获取基础包路径下所有类
     * @return Set
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取Service注解的类
     * @return Set
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = getClassSetByAnnotation(Service.class);
        return classSet;
    }

    /**
     * 获取Controller注解的类
     * @return Set
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = getClassSetByAnnotation(Controller.class);
        return classSet;
    }

    /**
     * 获取所有bean
     * @return Set
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }

    /**
     * 获取应用包下某父类(或接口)的所有子类(或实现类)
     * @param superClass
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下带有某注解的所有类
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
