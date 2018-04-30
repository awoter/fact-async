package com.woter.fact.async.core;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-8-1 下午4:12:08
 */
public interface AsyncFutureCallback<V> {

    /**
     * 执行成功回调方法
     *
     * @param result
     * @author woter
     * @date 2016-12-15 上午8:39:18
     * @version
     */
    void onSuccess(V result);

    /**
     * 执行失败回调方法
     *
     * @param t
     * @author woter
     * @date 2016-12-15 上午8:39:43
     * @version
     */
    void onFailure(Throwable t);

}
