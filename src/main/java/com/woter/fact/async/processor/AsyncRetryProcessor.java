package com.woter.fact.async.processor;

import com.woter.fact.async.bean.AsyncMethod;
import com.woter.fact.async.bean.AsyncRetry;
import com.woter.fact.async.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author woter
 * @date 2016-12-9 下午12:50:52
 */
public class AsyncRetryProcessor {

    private final static Logger logger = LoggerFactory.getLogger(AsyncRetryProcessor.class);

    public static <T> RetryResult<T> handler(Callable<T> callable, AsyncMethod asyncMethod) {
        RetryResult<T> result = new RetryResult<T>();
        AsyncRetry retry = asyncMethod.getRetry();
        if (retry == null) {
            return result;
        }
        T t = null;
        int i = 1;
        while (retry.getMaxAttemps() >= i) {
            logger.info("async processor trying to retry {} invocation; object:{} method:{}", i, asyncMethod.getObject(), asyncMethod.getMethod());
            result.setThrowable(null);
            try {
                t = callable.call();
            } catch (Throwable e) {
                result.setThrowable(e);
                logger.error("retry " + i + " invoke error", ReflectionHelper.getThrowableCause(e));
                if (!matchThrowable(retry, e)) {
                    break;
                }
            }
            ++i;
        }
        result.setData(t);
        return result;
    }

    private static boolean matchThrowable(AsyncRetry retry, Throwable e) {
        if (retry == null) {
            return false;
        }
        for (Class<?> clzss : retry.getExceptions()) {
            if (clzss.isAssignableFrom(e.getClass()) || clzss.equals(e.getClass())) {
                return true;
            }
        }
        return false;
    }
}
 