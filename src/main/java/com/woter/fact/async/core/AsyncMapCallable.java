package com.woter.fact.async.core;

import java.util.Map;

/**
 * @author woter
 * @date 2016-8-23 下午8:06:02
 */
public abstract class AsyncMapCallable<T> extends AsyncMap<T> {

    public AsyncMapCallable() {
    }

    public AsyncMapCallable(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public T doAsync() {
        return doAsync(dataMap);
    }

    public abstract T doAsync(Map<String, Object> dataMap);

}
 