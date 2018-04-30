package com.woter.fact.async.bean;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * 异步执行返回结果包装类</br>
 * 主要用于 void,array及Integer,Long,String,Boolean等Final修饰类
 *
 * </p>
 *
 * @author woter
 * @date 2016-4-12 下午4:19:14
 */
public class AsyncResult<T> implements Serializable {

    /**
     * <p>
     *
     *
     *
     * </p>
     *
     * @author woter
     * @date 2016-4-15 下午4:31:34
     * @version
     */
    private static final long serialVersionUID = -3289683114806441520L;
    private T data;

    public AsyncResult() {
    }

    public AsyncResult(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public AsyncResult<T> setData(T data) {
        this.data = data;
        return this;
    }

}
