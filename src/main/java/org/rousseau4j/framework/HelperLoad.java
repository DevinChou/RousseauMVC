package org.rousseau4j.framework;

import org.rousseau4j.framework.helper.*;
import org.rousseau4j.framework.util.ClassLoadUtil;

/**
 * Created by ZhouHangqi on 2017/7/28.
 */
public final class HelperLoad {

    public static void init() {
        Class<?>[] classes = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classes) {
            ClassLoadUtil.loadClass(cls.getName(), false);
        }
    }
}
