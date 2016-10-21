package com.woter.fact.async.cache; 

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.woter.fact.async.annotation.Async;
import com.woter.fact.async.bean.AsyncMethod;
import com.woter.fact.async.util.CommonUtil;
import com.woter.fact.async.util.ReflectionHelper;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-3-23 下午6:03:39
 * @version      
 */
public class AsyncProxyCache {
    
    private static ConcurrentMap<String,Class<?>> proxyClasss = new ConcurrentHashMap<String,Class<?>>(100);
    private static ConcurrentMap<String,AsyncMethod> proxyMethods = new ConcurrentHashMap<String,AsyncMethod>(1000);
    
    
    /**
     * 
     * <p>
     * 
     * 如果存在对应的key的ProxyClass就返回，没有则返回null
     *
     * </p>
     * @param key
     * @return
     *  
     * @author	hz15041240 
     * @date	2016-3-23 下午6:07:48
     * @version
     */
    public static Class<?> getProxyClass(String key) {
        return proxyClasss.get(key);
    }

    /**
     * 
     * <p>
     * 
     * 注册对应的proxyClass到Map
     *
     * </p>
     * @param key
     * @param proxyClass
     *  
     * @author	hz15041240 
     * @date	2016-3-23 下午6:07:57
     * @version
     */
    public static void registerProxy(String key, Class<?> proxyClass) {
	proxyClasss.putIfAbsent(key, proxyClass);
    }
    
    public static void putAsyncMethod(String key, AsyncMethod asyncMethod){
	proxyMethods.putIfAbsent(key, asyncMethod);
    }
    
    public static void registerMethod(Object bean, long timeout) {
	Method[] methods = bean.getClass().getDeclaredMethods();
	if (methods == null || methods.length == 0) {
	    return;
	}
	for (Method method : methods) {
	    Async annotation = ReflectionHelper.findAsyncAnnatation(bean, method);
	    if(annotation != null){
		AsyncMethod asyncMethod = new AsyncMethod(bean, method, annotation.timeout());
		putAsyncMethod(CommonUtil.buildkey(bean, method), asyncMethod);
	    }
	}
    }
    
    public static boolean containMethod(String key){
	return proxyMethods.containsKey(key);
    }
    
    public static AsyncMethod getAsyncMethod(String key){
	return proxyMethods.get(key);
    }
    
}
 