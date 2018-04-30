package com.woter.fact.async.bean;

/**
 * @author woter
 * @date 2016-12-9 上午11:26:17
 */
public class AsyncRetry {

    private int maxAttemps;

    private Class<?>[] exceptions;

    public AsyncRetry() {
    }

    public AsyncRetry(int maxAttemps, Class<?>... exceptions) {
        if (maxAttemps < 0) {
            maxAttemps = 0;
        }
        this.maxAttemps = maxAttemps;
        this.exceptions = exceptions;
    }

    public int getMaxAttemps() {
        return maxAttemps;
    }

    public void setMaxAttemps(int maxAttemps) {
        if (maxAttemps < 0) {
            maxAttemps = 0;
        }
        this.maxAttemps = maxAttemps;
    }

    public Class<?>[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(Class<?>[] exceptions) {
        this.exceptions = exceptions;
    }

    public boolean canRetry() {
        return maxAttemps > 0;
    }

}
