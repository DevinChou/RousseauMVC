package org.rousseau4j.framework.bean;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhouHangqi on 2017/7/30.
 */
public class View {

    /**
     * 视图路径
     */
    @Getter
    private String path;

    /**
     * 模型数据
     */
    @Getter
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<>();
    }

    public View add(String key, Object value) {
        model.put(key, value);
        return this;
    }
}
