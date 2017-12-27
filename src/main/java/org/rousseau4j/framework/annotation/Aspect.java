package org.rousseau4j.framework.annotation;

import java.lang.annotation.*;

/**
 * 切面注解
 * Created by ZhouHangqi on 2017/8/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();
}
