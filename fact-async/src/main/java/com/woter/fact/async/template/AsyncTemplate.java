package com.woter.fact.async.template; 

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.util.Assert;

import com.woter.fact.async.constant.AsyncConstant;
import com.woter.fact.async.core.AsyncExecutor;
import com.woter.fact.async.core.AsyncFuture;
import com.woter.fact.async.core.AsyncFutureCallback;
import com.woter.fact.async.core.AsyncTaskFuture;
import com.woter.fact.async.exception.AsyncException;
import com.woter.fact.async.pool.AsyncFutureTask;
import com.woter.fact.async.pool.AsyncPoolCallable;
import com.woter.fact.async.pool.AsyncRunnable;
import com.woter.fact.async.proxy.AsyncMethodProxy;
import com.woter.fact.async.proxy.AsyncProxy;
import com.woter.fact.async.proxy.AsyncResultProxy;
import com.woter.fact.async.util.ReflectionHelper;

/**
 * <p>
 * 
 * 编程式异步调用模板
 *
 * </p>
 * @author	woter 
 * @date	2016-3-31 下午4:27:27
 * @version      
 */
@SuppressWarnings("all")
public class AsyncTemplate {
    
    private static AsyncProxy cglibProxy = new AsyncMethodProxy();
    
    public enum ProxyType{
	CGLIB
    }
    
    /**
     * 
     * <p>
     * 
     * 获取代理方式：</br>
     * ProxyType.CGLIB 返回Cglib代理
     * 
     * </p>
     * @param type
     * @return
     *  
     * @author	woter 
     * @date	2016-4-14 上午10:42:37
     * @version
     */
    public static AsyncProxy getAsyncProxy(ProxyType type){
	return cglibProxy;
    }
    
    /**
     * 
     * <p>
     * 
     * 构建代理类</br>
     * 
     * </p>
     * @param t  需要被代理的类
     * @return T 必须带有返回参数且不支持void,array及Integer,Long,String,Boolean等Final修饰类</br>
     *  如果需要返回以上类型，可以创建对象包装；如：{@linkplain com.woter.fact.async.bean.AsyncResult}
     *  
     * @author	woter 
     * @date	2016-4-1 下午12:23:45
     * @version
     */
    public static <T> T buildProxy(T t) {
	return buildProxy(t,0);
    }
    
    /**
     * 
     * <p>
     * 
     * 构建代理类
     *
     * </p>
     * @param t 需要被代理的类
     * @param timeout 超时时间（单位：毫秒）
     * @return T 必须带有返回参数且不支持void,array及Integer,Long,String,Boolean等Final修饰类</br>
     *  如果需要返回以上类型，可以创建对象包装；如：{@linkplain com.woter.fact.async.bean.AsyncResult}
     *  
     * @author	woter 
     * @date	2016-4-14 上午10:04:58
     * @version
     */
    public static <T> T buildProxy(T t, long timeout) {
	return (T)getAsyncProxy(ProxyType.CGLIB).buildProxy(t, timeout,true);
    }
    
    
    
    
    /**
     * 
     * <p>
     * 
     * 构建代理类
     *
     * </p>
     * @param T 需要被代理的类
     * @param proxyType 代理类型
     * @return T 必须带有返回参数且不支持void,array及Integer,Long,String,Boolean等Final修饰类</br>
     *  如果需要返回以上类型，可以创建对象包装；如：{@linkplain com.woter.fact.async.bean.AsyncResult}
     *  
     * @author	woter 
     * @date	2016-4-14 上午10:06:19
     * @version
     */
    public static <T> T buildProxy(T t,ProxyType proxyType) {
	return (T)getAsyncProxy(proxyType).buildProxy(t, AsyncConstant.ASYNC_DEFAULT_TIME_OUT,true);
    }
    
    /**
     * 
     * <p>
     * 
     * 构建代理类
     *
     * </p>
     * @param T 需要被代理的类
     * @param timeout 超时时间（单位：毫秒）
     * @param proxyType 代理类型
     * @return T 必须带有返回参数且不支持void,array及Integer,Long,String,Boolean等Final修饰类</br>
     *  如果需要返回以上类型，可以创建对象包装；如：{@linkplain com.woter.fact.async.bean.AsyncResult}
     *  
     * @author	woter 
     * @date	2016-4-14 上午10:47:05
     * @version
     */
    public static <T> T buildProxy(T t,long timeout,ProxyType proxyType) {
	return (T)getAsyncProxy(proxyType).buildProxy(t, timeout,true);
    }
    
    /**
     * 
     * <p>
     * 
     * 异步执行 AsyncCallback.doAsync方法
     *
     * </p>
     * @param AsyncCallback<T> 需要实现的接口
     * @return T 必须带有返回参数且不支持void,array及Integer,Long,String,Boolean等Final修饰类</br>
     *  如果需要返回以上类型，可以创建对象包装；如：{@linkplain com.woter.fact.async.bean.AsyncResult}
     *  
     * @author	woter 
     * @date	2016-4-14 上午10:08:21
     * @version
     */
    public static <T> T execute(AsyncFuture<T> asyncFuture){
	return execute(asyncFuture,AsyncConstant.ASYNC_DEFAULT_TIME_OUT);
    }
    
    /**
     * 
     * <p>
     * 
     * 异步执行 AsyncCallback.doAsync方法
     *
     * </p>
     * @param AsyncCallback<T> 需要实现的接口
     * @param timeout 执行超时时间(单位：毫秒)
     * @return T 必须带有返回参数且不支持void,array及Integer,Long,String,Boolean等Final修饰类</br>
     *  如果需要返回以上类型，可以创建对象包装；如：{@linkplain com.woter.fact.async.bean.AsyncResult}
     *  
     * @author	woter 
     * @date	2016-4-14 上午10:35:48
     * @version
     */
    public static <T> T execute(AsyncFuture<T> asyncFuture,long timeout){
	Type type = asyncFuture.getClass().getGenericInterfaces()[0];
        if (!(type instanceof ParameterizedType)) {
            throw new AsyncException("you should specify AsyncCallback<T> for T type");
        }
        Class returnClass = (Class) ReflectionHelper.getGenericClass((ParameterizedType) type, 0);
        return execute(asyncFuture,returnClass,timeout);
    }
    
    /**
     * 
     * <p>
     * 
     * 异步执行AsyncRunnable.doAsync
     *
     * </p>
     * @param runable 实现AsyncRunnable接口
     *  
     * @author	woter 
     * @date	2016-7-28 下午2:34:03
     * @version
     */
    public static void execute(AsyncRunnable runable){
    	AsyncExecutor.submit(runable);
    }
    
    /**
     * 
     * <p>
     * 
     * 异步执行AsyncTask.doAsync；并且回调AsyncFutureCallback
     *
     * </p>
     * @param asyncFutrue
     * @param asyncFutureCallback
     *  
     * @author	woter 
     * @date	2016-8-1 下午5:20:22
     * @version
     */
    public static <T> void execute(final AsyncTaskFuture<T> asyncFutrue,AsyncFutureCallback<T> asyncFutureCallback){
    	
    	AsyncExecutor.submit(new AsyncPoolCallable<T>() {
			@Override
			public T call() throws Exception {
				return asyncFutrue.doAsync();
			}
		}, asyncFutureCallback);
    }
    
    private static <T> T execute(final AsyncFuture<T> callback,Class<?> returnClass,long timeout){
	Assert.notNull(callback);
	Assert.notNull(returnClass);
	if (Void.TYPE.isAssignableFrom(returnClass)) {
            return callback.doAsync();
        } else if (!Modifier.isPublic(returnClass.getModifiers())) {
            return callback.doAsync();
        } else if (Modifier.isFinal(returnClass.getModifiers())) {
            return callback.doAsync();
        } else if (returnClass.isPrimitive() || returnClass.isArray()) {
            return callback.doAsync();
        } else if (returnClass == Object.class) {
            return callback.doAsync();
        } else {
            AsyncFutureTask<T> future = AsyncExecutor.submit(new AsyncPoolCallable<T>() {
        	public T call() throws Exception {
                    try {
                        return callback.doAsync();
                    } catch (Throwable e) {
                        throw new AsyncException("future invoke error", e);
                    }
                }
            });
            return (T)new AsyncResultProxy(future).buildProxy(returnClass, timeout,true);
        }
    }
}

 