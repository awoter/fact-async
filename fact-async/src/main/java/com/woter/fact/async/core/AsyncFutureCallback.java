package com.woter.fact.async.core; 
/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	hz15041240 
 * @date	2016-8-1 下午4:12:08
 * @version      
 */
public interface AsyncFutureCallback<V>{
	
	public void onSuccess(V result);
	
	public void onFailure(Throwable t);
	
}
 