package org.rousseau4j.framework.bean;

import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.rousseau4j.framework.constant.RequestMethod;

/**
 * Created by ZhouHangqi on 2017/7/27.
 */
public class Request {

    /**
     * 请求方法
     */
    @Getter
    private RequestMethod requestMethod;

    /**
     * 请求路径
     */
    @Getter
    private String requestPath;

    public Request(RequestMethod requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
