package org.rousseau4j.framework.proxy;

import lombok.extern.log4j.Log4j;

import java.lang.reflect.Method;

/**
 * Created by ZhouHangqi on 2017/12/17.
 */
@Log4j
public abstract class AspectProxy implements Proxy{

    @Override
    public Object doProxy(ProxyChain proxyChain) {
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try{
            if (intercept(cls, method, params)) {
                before(cls, method, params);
                result = proxyChain.doProxyChain();
                after(cls, method, params, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("proxy failure", throwable);
            error(cls, method, params, throwable);
        } finally {
            end();
        }
        return result;
    }

    public abstract void begin();

    public abstract boolean intercept(Class<?> cls, Method method, Object[] params);

    public abstract void before(Class<?> cls, Method method, Object[] params);

    public abstract void after(Class<?> cls, Method method, Object[] params, Object result);

    public abstract void error(Class<?> cls, Method method, Object[] params, Throwable e);

    public abstract void end();
}
