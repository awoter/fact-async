package com.woter.fact.async.core; 

import com.woter.fact.async.bean.Profiler;

/**
 *  
 * @author	woter 
 * @date	2016-8-18 下午5:19:07
 * @version     
 */
public class AsyncProfiler {
    
    private static ThreadLocal<Profiler> threadMap = new ThreadLocal<Profiler>();

    
    public static Profiler getAndSet(Profiler profiler){
	if(profiler == null){
	    profiler = new Profiler(0);
	}
	threadMap.set(profiler);
	return profiler;
    }
    
    public static Profiler getAndIncrement(){
	Profiler profiler = threadMap.get();
	if(profiler == null){
	    return new Profiler(0);
	}else{
	    profiler.getAndIncrement();
	}
	return profiler;
    }
    
    public static Profiler get(){
	return getAndSet(threadMap.get());
    }
    
    
    public static void release(){
	threadMap.remove();
    }
}
 