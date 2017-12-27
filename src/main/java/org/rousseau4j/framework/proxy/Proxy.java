package org.rousseau4j.framework.proxy;

/**
 * 代理
 * Created by ZhouHangqi on 2017/8/8.
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain);
}
