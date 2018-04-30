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
 * @date 2016-7-28 下午2:08:30
 */
public abstract class AsyncMapRunnable extends AsyncMap<Void> {

    public AsyncMapRunnable() {
    }

    ;

    public AsyncMapRunnable(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public Void doAsync() {
        doAsync(dataMap);
        return null;
    }

    public abstract void doAsync(Map<String, Object> dataMap);

}
 