package org.rousseau4j.framework.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.rousseau4j.framework.annotation.Action;
import org.rousseau4j.framework.bean.Handler;
import org.rousseau4j.framework.bean.Request;
import org.rousseau4j.framework.constant.RequestMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZhouHangqi on 2017/7/27.
 */
public final class ControllerHelper {

    private static final Map<Request, Handler> ACTION_MAP = new ConcurrentHashMap<>();

    static {
        // 获取所有Controller注解类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtils.isNotEmpty(controllerClassSet)) {
            for (Class<?> controllerClass : controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        // 获取Controller类下的Action注解方法
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            // 获取请求路径和请求方法
                            String mapping = action.value();
                            RequestMethod[] requestMethods = action.type();
                            // 如果请求方法为空, 默认支持POST和GET方法
                            if (ArrayUtils.isEmpty(requestMethods)) {
                                requestMethods = new RequestMethod[]{RequestMethod.GET, RequestMethod.POST};
                            }
                            for (RequestMethod requestMethod : requestMethods) {
                                Request request = new Request(requestMethod, mapping);
                                Handler handler = new Handler(controllerClass, method);
                                Handler oldHandler = ACTION_MAP.get(request);
                                // 如果有Method的请求路径和方法同时重复，报异常
                                if (oldHandler != null && !oldHandler.equals(handler)) {
                                    throw new RuntimeException("cannot register repeat method");
                                }
                                ACTION_MAP.put(request, handler);
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(RequestMethod requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
