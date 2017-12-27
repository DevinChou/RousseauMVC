package org.rousseau4j.framework.bean;

import lombok.Getter;

import java.lang.reflect.Method;

/**
 * Created by ZhouHangqi on 2017/7/27.
 */
public class Handler {

    /**
     * Controller类
     */
    @Getter
    private Class<?> controllerClass;

    /**
     * Action方法
     */
    @Getter
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

}
