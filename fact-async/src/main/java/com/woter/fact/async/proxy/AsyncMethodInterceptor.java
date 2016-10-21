package com.woter.fact.async.proxy;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.woter.fact.async.bean.AsyncMethod;
import com.woter.fact.async.cache.AsyncProxyCache;
import com.woter.fact.async.constant.AsyncConstant;
import com.woter.fact.async.core.AsyncExecutor;
import com.woter.fact.async.exception.AsyncException;
import com.woter.fact.async.pool.AsyncFutureTask;
import com.woter.fact.async.pool.AsyncPoolCallable;
import com.woter.fact.async.pool.AsyncRunnable;
import com.woter.fact.async.template.AsyncTemplate;
import com.woter.fact.async.util.CommonUtil;
import com.woter.fact.async.util.ReflectionHelper;

/**
 * <p>
 * 
 * 
 * 
 * </p>
 * 
 * @author hz15041240
 * @date 2016-3-23 下午6:13:58
 * @version
 */
public class AsyncMethodInterceptor implements MethodInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(AsyncMethodInterceptor.class);

    private long timeout;

    private Object targetObject;
    
    private boolean all;

    public AsyncMethodInterceptor(Object targetObject, long timeout,boolean all) {
	this.timeout = timeout;
	this.targetObject = targetObject;
	this.all = all;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
	if (AsyncConstant.ASYNC_DEFAULT_TRACE_LOG) {
	    logger.debug("start call obejct:{} method:{}", CommonUtil.getClass(targetObject).getName(), CommonUtil.buildMethod(method));
	}
	if (AsyncExecutor.isDestroyed()) {
	    return ReflectionHelper.invoke(targetObject, args, method);
	}
	String key = CommonUtil.buildkey(targetObject, method);
	if (!all && !AsyncProxyCache.containMethod(key)) {
	    return ReflectionHelper.invoke(targetObject, args, method);
	}

	final Object finObj = targetObject;
	final Object[] finArgs = args;
	final Method finMethod = method;

	long timeout = this.timeout;
	
	Class<?> returnClass = method.getReturnType();
	if (Void.TYPE.isAssignableFrom(returnClass)) {
	    AsyncTemplate.execute(new AsyncRunnable() {
		@Override
		public void doAsync(Map<String, Object> dataMap) {
		    try {
			ReflectionHelper.invoke(finObj, finArgs, finMethod);
		    } catch (Throwable e) {
			logger.error("async runnable invoke error", e);
		    }
		}
	    });
	    return null;
	}

	final AsyncMethod asyncMethod = AsyncProxyCache.getAsyncMethod(key);
	if (asyncMethod != null) {
	    timeout = asyncMethod.getTimeout();
	}
	AsyncFutureTask<Object> future = AsyncExecutor.submit(new AsyncPoolCallable<Object>() {
	    public Object call() throws Exception {
		Object object = null;
		try {
		    if (asyncMethod != null) {
			if (AsyncConstant.ASYNC_DEFAULT_TRACE_LOG) {
			    logger.debug("start async call object:{} asyncMethod:{}", asyncMethod.getObject().getClass().getName(), CommonUtil.buildMethod(asyncMethod.getMethod()));
			}
			object = ReflectionHelper.invoke(asyncMethod.getObject(), finArgs, asyncMethod.getMethod());
			if (AsyncConstant.ASYNC_DEFAULT_TRACE_LOG) {
			    logger.debug("end async call object:{} asyncMethod:{}", asyncMethod.getObject().getClass().getName(), CommonUtil.buildMethod(asyncMethod.getMethod()));
			}
		    } else {
			if (AsyncConstant.ASYNC_DEFAULT_TRACE_LOG) {
			    logger.debug("start call object:{} method:{}", finObj.getClass().getName(), CommonUtil.buildMethod(finMethod));
			}
			object = ReflectionHelper.invoke(finObj, finArgs, finMethod);
			if (AsyncConstant.ASYNC_DEFAULT_TRACE_LOG) {
			    logger.debug("end call object:{} method:{}", finObj.getClass().getName(), CommonUtil.buildMethod(finMethod));
			}
		    }
		    return object;
		} catch (Throwable e) {
		    throw new AsyncException("future invoke error", e);
		}
	    }
	});
	return new AsyncResultProxy(future).buildProxy(returnClass, timeout, true);

    }
}
