package org.rousseau4j.framework.bean;

import lombok.Getter;

/**
 * Created by ZhouHangqi on 2017/7/30.
 */
public class Data {

    /**
     * 模型数据
     */
    @Getter
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

}
