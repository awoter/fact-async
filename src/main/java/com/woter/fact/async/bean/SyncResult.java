package com.woter.fact.async.bean; 

/**
 *  
 * @author	woter 
 * @date	2016-8-24 下午6:05:21
 * @version     
 */
public class SyncResult<T> {
    
    private T value;
    
    private Throwable throwable;
    
    public T getValue() {
        return value;
    }
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public Throwable getThrowable() {
        return throwable;
    }
    
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
    
    public boolean isSucceed(){
	return throwable == null;
    }
    
}
 