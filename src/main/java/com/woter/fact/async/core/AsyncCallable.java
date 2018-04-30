package com.woter.fact.async.core;

import com.woter.fact.async.constant.AsyncConstant;


/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-23 下午2:08:13
 */

public abstract class AsyncCallable<T> implements AsyncFutureCallable<T> {

    @Override
    public T call() {
        return doAsync();
    }

    public abstract T doAsync();

    /**
     * 调用超时设置-单位毫秒(默认0-不超时)
     */
    @Override
    public long timeout() {
        return AsyncConstant.ASYNC_DEFAULT_TIME_OUT;
    }

    /**
     * 最多重试次数
     */
    @Override
    public int maxAttemps() {
        return AsyncConstant.ASYNC_DEFAULT_RETRY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Throwable>[] exceptions() {
        return new Class[]{Throwable.class};
    }

    @Override
    public final String cacheKey() {
        return null;
    }

}
 