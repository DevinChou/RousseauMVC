package org.rousseau4j.framework.util;

import org.rousseau4j.framework.exception.IllegalRequestParameterException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhouHangqi on 2017/8/1.
 */
public final class MapUtil {


    /**
     * 转换key-value形式,将A.B.C:1的K-V转成A:(B:(C:1))
     * @param paramName
     * @param paramValue
     * @param res
     */
    public static void castKeyValue(String paramName, String paramValue, Map<String, Object> res) {
        String[] names = paramName.split("\\.");
        Map<String, Object> root = res;
        for (int i = 0; i < names.length; i++) {
            Object value;
            value = root.get(names[i]);
            if (value == null) {
                if (i + 1 == names.length) {
                    root.put(names[i], paramValue);
                } else {
                    root.put(names[i], new HashMap<String, Object>());
                    root = (Map<String, Object>) root.get(names[i]);
                }
            } else if (value instanceof Map) {
                if (i + 1 == names.length) {
                    throw new IllegalRequestParameterException(paramName + " cannot resolve");
                }
                root = (Map<String, Object>) value;
            } else if (value instanceof String) {
                throw new IllegalRequestParameterException(paramName + " cannot resolve");
            }
        }
    }
}
