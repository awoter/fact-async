package com.woter.fact.async.core;

import java.util.Map;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-8-2 下午5:24:13
 */
public abstract class AsyncMap<T> extends AsyncCallable<T> {

    protected Map<String, Object> dataMap;

    void setDateMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

}
 