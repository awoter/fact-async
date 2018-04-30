package com.woter.fact.async.core;

import com.woter.fact.async.constant.AsyncConstant;


/**
 * @author woter
 * @date 2017-7-17 下午5:36:08
 */
public abstract class AsyncFunction<T, E> {


    public abstract E doAsync(T t);


    public long timeout() {
        return AsyncConstant.ASYNC_DEFAULT_TIME_OUT;
    }

    public int maxAttemps() {
        return AsyncConstant.ASYNC_DEFAULT_RETRY;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Throwable>[] exceptions() {
        return new Class[]{Throwable.class};
    }

}
 