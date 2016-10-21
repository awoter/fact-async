package com.woter.fact.async.core; 

import java.util.Map;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	hz15041240 
 * @date	2016-8-2 下午5:24:13
 * @version      
 */
public abstract class AsyncTask {
    
    protected Map<String,Object> dataMap;
    
    public void setDateMap(Map<String,Object> dataMap){
	this.dataMap = dataMap;
    };
}
 