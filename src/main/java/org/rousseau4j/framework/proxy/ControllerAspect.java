package org.rousseau4j.framework.proxy;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.rousseau4j.framework.annotation.Aspect;
import org.rousseau4j.framework.annotation.Controller;

import java.lang.reflect.Method;

/**
 * Created by ZhouHangqi on 2017/12/17.
 */
@Slf4j
@Aspect(Controller.class)
public class ControllerAspect extends DefaultAspectProxy {

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) {
        log.debug("----------- begin -----------");
        log.debug(String.format("class: %s", cls.getName()));
        log.debug(String.format("method: %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) {
        log.debug(String.format("time: %dms", System.currentTimeMillis() - begin));
        log.debug("----------- end -----------");
    }
}
