package com.woter.fact.async.proxy;

import com.woter.fact.async.cache.AsyncProxyCache;
import com.woter.fact.async.constant.AsyncConstant;
import com.woter.fact.async.exception.AsyncException;
import com.woter.fact.async.util.CommonUtil;
import com.woter.fact.async.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-31 下午4:12:15
 */
@SuppressWarnings("all")
public class AsyncMethodProxy implements AsyncProxy {

    private final static Logger logger = LoggerFactory.getLogger(AsyncMethodProxy.class);

    @Override
    public Object buildProxy(Object target, boolean all) {
        return buildProxy(target, AsyncConstant.ASYNC_DEFAULT_TIME_OUT, all);
    }

    @Override
    public Object buildProxy(Object target, long timeout, boolean all) {
        Class<?> targetClass = CommonUtil.getClass(target);
        if (target instanceof Class) {
            throw new AsyncException("target is not object instance");
        }

        if (!ReflectionHelper.canProxy(targetClass)) {
            return target;
        }
        Class<?> proxyClass = AsyncProxyCache.getProxyClass(CommonUtil.buildkey(targetClass.getName(), all));
        if (proxyClass == null) {
            Enhancer enhancer = new Enhancer();
            if (targetClass.isInterface()) {
                enhancer.setInterfaces(new Class[]{targetClass});
            } else {
                enhancer.setSuperclass(targetClass);
            }
            enhancer.setNamingPolicy(AsyncNamingPolicy.INSTANCE);
            enhancer.setCallbackType(AsyncMethodInterceptor.class);
            proxyClass = enhancer.createClass();
            logger.debug("create proxy class:{}", targetClass);
            AsyncProxyCache.registerProxy(CommonUtil.buildkey(targetClass.getName(), all), proxyClass);
            AsyncProxyCache.registerMethod(target, timeout, all);
        }
        Enhancer.registerCallbacks(proxyClass, new Callback[]{new AsyncMethodInterceptor(target)});
        Object proxyObject = null;
        try {
            proxyObject = ReflectionHelper.newInstance(proxyClass);
        } finally {
            Enhancer.registerStaticCallbacks(proxyClass, null);
        }

        return proxyObject;
    }

}
