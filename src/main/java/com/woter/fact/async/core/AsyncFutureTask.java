package com.woter.fact.async.core;

import com.woter.fact.async.bean.AsyncMethod;
import com.woter.fact.async.processor.AsyncCallbackProcessor;
import com.woter.fact.async.processor.AsyncRetryProcessor;
import com.woter.fact.async.processor.RetryResult;
import com.woter.fact.async.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-23 下午2:16:04
 */
public class AsyncFutureTask<V> extends FutureTask<V> {

    private final static Logger logger = LoggerFactory.getLogger(AsyncFutureTask.class);

    private long startTime = 0;

    private long endTime = 0;

    private volatile V value;

    private int counter;

    private AsyncMethod method;

    private Callable<V> callable;

    private AsyncFutureCallback<V> futureCallback;

    private final ReentrantLock mainLock = new ReentrantLock();

    private final Condition condition = mainLock.newCondition();

    public AsyncFutureTask(Callable<V> callable, AsyncFutureCallback<V> futureCallback, AsyncMethod method) {
        super(callable);
        this.callable = callable;
        this.method = method;
        counter = AsyncCounter.intValue();
        if (futureCallback != null) {
            this.futureCallback = futureCallback;
        }
    }

    @Override
    protected void done() {
        endTime = System.currentTimeMillis();
        if (counter >= 0) {
            AsyncCounter.release();
        }
        RetryResult<V> result = new RetryResult<V>();
        if (super.isCancelled()) {
            AsyncCallbackProcessor.doCallback(futureCallback, result.setThrowable(new TimeoutException()));
            return;
        }

        if (needCallbackAndRetry()) {
            try {
                result.setData(value = innerGetValue(method.getTimeout(), TimeUnit.MILLISECONDS));
            } catch (Throwable e) {
                result.setThrowable(e);
                if (method.isVoid() || getMaxAttemps() > 0) {
                    logger.error("future invoke error", ReflectionHelper.getThrowableCause(e));
                }
                result = AsyncRetryProcessor.handler(callable, method);
                value = result.getData();
            } finally {
                if (needLock()) {
                    final ReentrantLock mainLock = this.mainLock;
                    mainLock.lock();
                    try {
                        condition.signal();
                    } finally {
                        mainLock.unlock();
                    }
                }
                AsyncCallbackProcessor.doCallback(futureCallback, result);
            }
        }
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        if (counter >= 0) {
            AsyncCounter.set(++counter);
        }
        super.run();
    }

    public void syncRun() {
        counter = -1;
        run();
    }

    public V getValue(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        timeout = method.getTimeout();
        if (needCallbackAndRetry()) {
            if (needLock()) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    if (value == null) {
                        if (timeout > 0)
                            condition.await(timeout, unit);
                        else
                            condition.await();
                    }
                } finally {
                    mainLock.unlock();
                }
                if (value == null)
                    throw new TimeoutException();
            }
        }
        return innerGetValue(timeout, unit);
    }

    private V innerGetValue(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        if (value == null) {
            long startRunTime = System.currentTimeMillis();
            if (timeout <= 0) {
                value = super.get();
            } else {
                value = super.get(timeout, unit);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("invoking time:{} load time:{} timeout:{}", this.endTime - this.startTime, System.currentTimeMillis() - startRunTime, timeout);
            }
        }
        return value;
    }

    private boolean needLock() {
        if (method.getTimeout() > 0 && !method.isVoid() && getMaxAttemps() > 0) {
            return true;
        }
        return false;
    }

    private boolean needCallbackAndRetry() {
        if (futureCallback != null || getMaxAttemps() > 0 || method.isVoid()) {
            return true;
        }
        return false;
    }

    public int getMaxAttemps() {
        if (method.getRetry() == null)
            return 0;
        return method.getRetry().getMaxAttemps();
    }

    public int getCounter() {
        return counter;
    }

}
