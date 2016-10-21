package com.woter.fact.async.core; 

import java.util.Map;

/**
 *  
 * @author	woter 
 * @date	2016-8-23 下午8:06:02
 * @version     
 */
public abstract class AsyncTaskFuture<T> extends AsyncTask implements AsyncFuture<T> {
    
    public AsyncTaskFuture(){}
    
    public AsyncTaskFuture(Map<String,Object> dataMap){
	this.dataMap = dataMap;
    }
    
    @Override
    public T doAsync() {
	return doAsync(dataMap);
    }
    
    public abstract T doAsync(Map<String,Object> dataMap);
    
}
 