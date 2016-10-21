package com.woter.fact.async.proxy; 

import org.springframework.cglib.core.DefaultNamingPolicy;

/**
 * @author	hz15041240 
 * @date	2016-9-2 上午11:06:06
 * @version      
 */
/**
 *  
 * @author	hz15041240 
 * @date	2016-9-2 上午11:06:06
 * @version     
 */
public class AsyncNamingPolicy extends DefaultNamingPolicy{
    	public static final AsyncNamingPolicy INSTANCE = new AsyncNamingPolicy();

	@Override
	protected String getTag() {
		return "ByAsyncCGLIB";
	}
}
 