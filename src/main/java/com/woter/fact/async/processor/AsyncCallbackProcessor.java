package com.woter.fact.async.processor;

import com.woter.fact.async.core.AsyncFutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author woter
 * @date 2016-12-19 下午12:06:20
 */
public class AsyncCallbackProcessor {

    private final static Logger logger = LoggerFactory.getLogger(AsyncCallbackProcessor.class);

    public static <V> void doCallback(AsyncFutureCallback<V> futureCallback, RetryResult<V> result) {

        if (futureCallback != null) {
            try {
                if (result.getThrowable() != null) {
                    futureCallback.onFailure(result.getThrowable());
                } else {
                    futureCallback.onSuccess(result.getData());
                }
            } catch (Throwable e) {
                logger.error("async callback error", e);
            }
        }
    }

}
