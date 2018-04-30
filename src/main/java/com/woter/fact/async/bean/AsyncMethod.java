package com.woter.fact.async.bean;

import com.woter.fact.async.util.ReflectionHelper;

import java.lang.reflect.Method;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-4-13 下午6:00:36
 */
public class AsyncMethod {

    private long timeout;
    private boolean isVoid;
    private Object object;
    private Method method;
    private com.woter.fact.async.bean.AsyncRetry retry;

    public AsyncMethod(Object object, Method method, long timeout) {
        this.object = object;
        this.method = method;
        this.timeout = timeout;
        this.isVoid = ReflectionHelper.isVoid(method);
    }

    public AsyncMethod(Object object, Method method, long timeout, com.woter.fact.async.bean.AsyncRetry retries) {
        this.object = object;
        this.method = method;
        this.timeout = timeout;
        this.retry = retries;
        this.isVoid = ReflectionHelper.isVoid(method);
    }

    public Method getMethod() {
        return method;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void setMethod(Method method) {
        this.method = method;
        this.isVoid = ReflectionHelper.isVoid(method);
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }


    public Object getObject() {
        return object;
    }


    public void setObject(Object object) {
        this.object = object;
    }


    public com.woter.fact.async.bean.AsyncRetry getRetry() {
        return retry;
    }


    public void setRetry(com.woter.fact.async.bean.AsyncRetry retry) {
        this.retry = retry;
    }

    public void setVoid(boolean isVoid) {
        this.isVoid = isVoid;
    }
}
 