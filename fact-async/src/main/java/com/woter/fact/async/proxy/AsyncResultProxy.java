package com.woter.fact.async.proxy; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;

import com.woter.fact.async.cache.AsyncProxyCache;
import com.woter.fact.async.constant.AsyncConstant;
import com.woter.fact.async.pool.AsyncFutureTask;
import com.woter.fact.async.util.ReflectionHelper;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	hz15041240 
 * @date	2016-3-31 下午3:42:37
 * @version      
 */
@SuppressWarnings("all")
public class AsyncResultProxy implements AsyncProxy {
    
    private final static Logger logger = LoggerFactory.getLogger(AsyncResultProxy.class);
    
    private AsyncFutureTask future;
    
    public AsyncResultProxy(AsyncFutureTask future){
	this.future = future;
    }
    
    public Object buildProxy(Object t,boolean all){
	return buildProxy(t,AsyncConstant.ASYNC_DEFAULT_TIME_OUT,true);
    }
    
    public Object buildProxy(Object t,long timeout,boolean all){
	Class<?> returnClass = t.getClass();
	if(t instanceof Class){
	    returnClass = (Class)t;
	}
	Class<?> proxyClass  = AsyncProxyCache.getProxyClass(returnClass.getName());
        if (proxyClass == null) {
            Enhancer enhancer = new Enhancer();
            if (returnClass.isInterface()) {
                enhancer.setInterfaces(new Class[] { returnClass });
            } else {
                enhancer.setSuperclass(returnClass);
            }
            enhancer.setNamingPolicy(AsyncNamingPolicy.INSTANCE);
            enhancer.setCallbackType(AsyncResultInterceptor.class);
            proxyClass = enhancer.createClass();
            logger.debug("create result proxy class:{}",returnClass);
            AsyncProxyCache.registerProxy(returnClass.getName(), proxyClass);
        }
        Enhancer.registerCallbacks(proxyClass,new Callback[]{new AsyncResultInterceptor(future,timeout)});
	Object proxyObject = null;
	try{
	    proxyObject = ReflectionHelper.newInstance(proxyClass);
	}finally{
	    Enhancer.registerStaticCallbacks(proxyClass, null);
	}
        return proxyObject;
    }
}
 