package com.woter.fact.async.cache;

import com.woter.fact.async.annotation.Async;
import com.woter.fact.async.bean.AsyncMethod;
import com.woter.fact.async.bean.AsyncRetry;
import com.woter.fact.async.util.CommonUtil;
import com.woter.fact.async.util.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-23 下午6:03:39
 */
public final class AsyncProxyCache {

    private static ConcurrentMap<String, Class<?>> proxyClasss = new ConcurrentHashMap<String, Class<?>>(100);
    private static ConcurrentMap<String, Object> nativeObjects = new ConcurrentHashMap<String, Object>(100);
    private static ConcurrentMap<String, AsyncMethod> proxyMethods = new ConcurrentHashMap<String, AsyncMethod>(500);


    /**
     * <p>
     * <p>
     * 如果存在对应的key的ProxyClass就返回，没有则返回null
     *
     * </p>
     *
     * @param key
     * @return
     * @author woter
     * @date 2016-3-23 下午6:07:48
     * @version
     */
    public static Class<?> getProxyClass(String key) {
        return proxyClasss.get(key);
    }

    /**
     * <p>
     * <p>
     * 注册对应的proxyClass到Map
     *
     * </p>
     *
     * @param key
     * @param proxyClass
     * @author woter
     * @date 2016-3-23 下午6:07:57
     * @version
     */
    public static void registerProxy(String key, Class<?> proxyClass) {
        proxyClasss.putIfAbsent(key, proxyClass);
    }

    public static void putAsyncMethod(String key, AsyncMethod asyncMethod) {
        proxyMethods.putIfAbsent(key, asyncMethod);
    }

    public static void registerMethod(Object bean, long timeout, boolean all) {
        Method[] methods = CommonUtil.getClass(bean).getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return;
        }

        if (!all) {
            nativeObjects.putIfAbsent(CommonUtil.getClass(bean).getName(), bean);
        } else {
            Object nativeObject = nativeObjects.get(CommonUtil.getClass(bean).getName());
            if (nativeObject != null) bean = nativeObject;
        }

        for (Method method : methods) {
            if (!all) {
                Async annotation = ReflectionHelper.findAsyncAnnatation(bean, method);
                if (annotation != null) {
                    AsyncMethod asyncMethod = new AsyncMethod(bean, method, annotation.timeout(), new AsyncRetry(annotation.maxAttemps(), annotation.exceptions()));
                    putAsyncMethod(CommonUtil.buildkey(bean, method), asyncMethod);
                }
            } else {
                Class<?> returnClass = method.getReturnType();
                if (Void.TYPE.isAssignableFrom(returnClass) || ReflectionHelper.canProxy(returnClass)) {
                    AsyncMethod asyncMethod = new AsyncMethod(bean, method, timeout, new AsyncRetry(0, Throwable.class));
                    putAsyncMethod(CommonUtil.buildkey(bean, method), asyncMethod);
                }
            }
        }
    }

    public static boolean containMethod(String key) {
        return proxyMethods.containsKey(key);
    }

    public static AsyncMethod getAsyncMethod(String key) {
        return proxyMethods.get(key);
    }

}
 