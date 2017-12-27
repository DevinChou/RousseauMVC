package org.rousseau4j.framework.annotation;

import org.rousseau4j.framework.constant.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路径注解
 * Created by ZhouHangqi on 2017/7/26.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

    /**
     * 请求类型
     * @return
     */
    RequestMethod[] type() default {};

    /**
     * 路径
     * @return
     */
    String value();
}
