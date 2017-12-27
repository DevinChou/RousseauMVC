package org.rousseau4j.framework.proxy;

import java.lang.reflect.Method;

/**
 * Created by ZhouHangqi on 2017/12/17.
 */
public class DefaultAspectProxy extends AspectProxy {

    @Override
    public void begin() {

    }

    @Override
    public boolean intercept(Class<?> cls, Method method, Object[] params) {
        return true;
    }

    @Override
    public void before(Class<?> cls, Method method, Object[] params) {

    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) {

    }

    @Override
    public void error(Class<?> cls, Method method, Object[] params, Throwable e) {

    }

    @Override
    public void end() {

    }
}
