package com.woter.fact.async.pool;

/**
 * @author woter
 * @date 2017-12-3 上午10:04:58
 */
public interface RunnableAround {

    public Runnable advice(Runnable command);

}
 