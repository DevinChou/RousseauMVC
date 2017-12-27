package org.rousseau4j.framework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.rousseau4j.framework.bean.Handler;
import org.rousseau4j.framework.annotation.Param;
import org.rousseau4j.framework.bean.Data;
import org.rousseau4j.framework.bean.View;
import org.rousseau4j.framework.constant.RequestMethod;
import org.rousseau4j.framework.exception.IllegalRequestParameterException;
import org.rousseau4j.framework.helper.BeanHelper;
import org.rousseau4j.framework.helper.ConfigHelper;
import org.rousseau4j.framework.helper.ControllerHelper;
import org.rousseau4j.framework.util.CodecUtil;
import org.rousseau4j.framework.util.MapUtil;
import org.rousseau4j.framework.util.ReflectionUtil;
import org.rousseau4j.framework.util.StreamUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created by ZhouHangqi on 2017/7/30.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化相关类
        HelperLoad.init();
        // 注册jsp和静态文件路径
        ServletContext servletContext = config.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        ServletRegistration staticServlet = servletContext.getServletRegistration("default");
        staticServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取请求方法和请求路径
        String requestMethod = request.getMethod().toUpperCase();
        String requestPath = request.getPathInfo();
        // 获取handler
        Handler handler = null;
        for (RequestMethod method : RequestMethod.values()) {
            if (method.name().equals(requestMethod)) {
                handler = ControllerHelper.getHandler(method, requestPath);
            }
        }
        if (handler == null) {
            response.sendError(404, "unknown request path");
            return;
        }
        // 封装请求参数
        Map<String, Object> requestParamMap = createParamMap(request);
        // 获取Bean实例
        Class<?> controllerClass = handler.getControllerClass();
        Object controller = BeanHelper.getBean(controllerClass);
        // 获取方法
        Method action = handler.getActionMethod();
        Parameter[] parameters = action.getParameters();
        List<Object> paramBeans = new ArrayList<>(parameters.length);
        if (ArrayUtils.isNotEmpty(parameters)) {
            for (Parameter parameter : parameters) {
                // 注入请求参数
                if (parameter.isAnnotationPresent(Param.class)) {
                    Param param = parameter.getAnnotation(Param.class);
                    Object paramValue = requestParamMap.get(param.value());
                    if (paramValue == null) {
                        paramBeans.add(null);
                    } else {
                        try {
                            paramBeans.add(JSON.parseObject(JSONObject.toJSONString(paramValue), parameter.getType()));
                        } catch (JSONException e) {
                            throw new IllegalRequestParameterException("Param " + param.value() + " cannot resolve");
                        }
                    }
                } else {
                    if (parameter.getType() == HttpServletRequest.class) {
                        paramBeans.add(request);
                    } else if (parameter.getType() == HttpServletResponse.class) {
                        paramBeans.add(response);
                    } else {
                        paramBeans.add(null);
                    }
                }
            }
        }
        Object result = ReflectionUtil.invokeMethod(controller, action, paramBeans.toArray());
        // 处理Action方法返回值
        if (result instanceof View) {
            View view = (View) result;
            String path = view.getPath();
            if (StringUtils.isNotBlank(path)) {
                if (path.startsWith("/")) {
                    response.sendRedirect(request.getContextPath() + path);
                } else {
                    Map<String, Object> model = view.getModel();
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                    request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
                }
            }
        } else if (result instanceof Data){
            Data data = (Data) result;
            Object model = data.getModel();
            if (model != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                String json = JSONObject.toJSONString(model);
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 封装请求参数
     * 1. 当form表单内容采用 enctype=application/x-www-form-urlencoded编码时，
     * 先通过调用request.getParameter()方法得到参数后，再调用request.getInputStream()或request.getReader()已经得不到流中的内容，
     * 因为在调用 request.getParameter()时系统可能对表单中提交的数据以流的形式读了一次,反之亦然。
     *
     *2. 当form表单内容采用enctype=multipart/form-data编码时，即使先调用request.getParameter()也得不到数据，
     * 但是这时调用request.getParameter()方法对 request.getInputStream()或request.getReader()没有冲突，
     * 即使已经调用了 request.getParameter()方法也可以通过调用request.getInputStream()或request.getReader()得到表单中的数据,
     * 而request.getInputStream()和request.getReader()在同一个响应中是不能混合使用的,如果混合使用就会抛异常。
     * @param request
     * @return
     * @throws IOException
     */
    private Map<String, Object> createParamMap(HttpServletRequest request) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            MapUtil.castKeyValue(paramName, request.getParameter(paramName), paramMap);
        }
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtils.isNotBlank(body)) {
            String[] params = StringUtils.split(body, "&");
            if (ArrayUtils.isNotEmpty(params)) {
                for (String param : params) {
                    String[] array = StringUtils.split(param, "=");
                    if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                        // 转换key-value形式,将A.B.C:1的K-V转成A:(B:(C:1))
                        MapUtil.castKeyValue(array[0], array[1], paramMap);
                    }
                }
            }
        }
        return paramMap;
    }
}
