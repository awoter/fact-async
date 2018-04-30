package com.woter.fact.async.core;

import java.util.concurrent.Callable;


/**
 * @author woter
 * @date 2016-12-13 下午8:48:43
 */
public interface AsyncFutureCallable<V> extends Callable<V> {

    long timeout();

    int maxAttemps();

    Class<? extends Throwable>[] exceptions();

    String cacheKey();
}
 