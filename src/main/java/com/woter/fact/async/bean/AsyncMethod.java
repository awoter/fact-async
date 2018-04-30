package com.woter.fact.async.bean; 

import java.lang.reflect.Method;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-4-13 下午6:00:36
 * @version      
 */
public class AsyncMethod {
    
    public AsyncMethod(Object object,Method method,long timeout){
	this.object = object;
	this.method = method;
	this.timeout = timeout;
    } 
    
    private Object object;
    private Method method;
    private long timeout;
    
    public Method getMethod() {
        return method;
    }
    
    public void setMethod(Method method) {
        this.method = method;
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
}
 