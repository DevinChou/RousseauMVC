package org.rousseau4j.framework.transaction;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.rousseau4j.framework.annotation.Aspect;
import org.rousseau4j.framework.annotation.Service;
import org.rousseau4j.framework.annotation.Transaction;
import org.rousseau4j.framework.proxy.DefaultAspectProxy;

import java.lang.reflect.Method;

/**
 * Created by ZhouHangqi on 2018/1/1.
 */
@Slf4j
@Aspect(Service.class)
public class TransactionAspect extends DefaultAspectProxy {

    private static final ThreadLocal<Boolean> FLAG = ThreadLocal.withInitial(() -> false);

    @Override
    public boolean intercept(Class<?> cls, Method method, Object[] params) {
        boolean flag = FLAG.get();
        return !flag && method.isAnnotationPresent(Transaction.class);
    }

    @Override
    public void before(Class<?> cls, Method method, Object[] params) {
        FLAG.set(true);
        DateBaseUtil.beginTransaction();
        log.debug("begin transaction");
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) {
        DateBaseUtil.commitTransaction();
        log.debug("commit transaction");
    }

    @Override
    public void error(Class<?> cls, Method method, Object[] params, Throwable e) {
        DateBaseUtil.rollbackTransaction();
        log.debug("rollback transaction");
    }

    @Override
    public void end() {
        FLAG.remove();
    }
}
