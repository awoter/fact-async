package com.woter.fact.async.processor;

/**
 * @author woter
 * @date 2016-12-13 下午4:06:14
 */
public class RetryResult<T> {

    private T data;

    private Throwable throwable;

    public RetryResult() {
    }

    public RetryResult(T data, Throwable throwable) {
        this.data = data;
        this.throwable = throwable;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public Throwable getThrowable() {
        return throwable;
    }

    public RetryResult<T> setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

}
 