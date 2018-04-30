/**
 * Copyright (c) 2006-2016 Hzins Ltd. All Rights Reserved.
 * <p>
 * This code is the confidential and proprietary information of
 * Hzins. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Hzins,http://www.hzins.com.
 */
package com.woter.fact.async.core;

import com.woter.fact.async.bean.AsyncMethod;
import com.woter.fact.async.bean.AsyncRetry;
import com.woter.fact.async.cache.AsyncProxyCache;
import com.woter.fact.async.config.AsyncConfigurer;
import com.woter.fact.async.config.DefaultAsyncConfigurer;
import com.woter.fact.async.config.ThreadPoolConfiguration;
import com.woter.fact.async.constant.HandleMode;
import com.woter.fact.async.exception.AsyncException;
import com.woter.fact.async.inject.TransactionBuilder;
import com.woter.fact.async.pool.AsyncTaskThreadPool;
import com.woter.fact.async.pool.NamedThreadFactory;
import com.woter.fact.async.pool.RunnableAround;
import com.woter.fact.async.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-23 上午11:22:17
 */
public final class AsyncExecutor {

    private final static Logger logger = LoggerFactory.getLogger(AsyncExecutor.class);

    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final AtomicBoolean destroyed = new AtomicBoolean(false);

    private static AsyncTaskThreadPool threadPool;
    private static TransactionBuilder transactionBuilder;


    public static void checkArgument(ThreadPoolConfiguration configuration) {
        Assert.notNull(configuration, "thread pool configuration propertie not be null");
        Assert.notNull(configuration.getAllowCoreThreadTimeout(), "configuration propertie async.allowCoreThreadTimeout not be null");
        Assert.notNull(configuration.getCorePoolSize(), "configuration propertie async.allowCoreThreadTimeout not be null");
        Assert.notNull(configuration.getKeepAliveTime(), "configuration propertie async.allowCoreThreadTimeout not be null");
        Assert.notNull(configuration.getMaxAcceptCount(), "configuration propertie async.allowCoreThreadTimeout not be null");
        Assert.notNull(configuration.getMaxPoolSize(), "configuration propertie async.allowCoreThreadTimeout not be null");
        Assert.hasText(configuration.getRejectedExecutionHandler(), "configuration propertie async.allowCoreThreadTimeout not be null");
    }

    public static void initializeThreadPool(ThreadPoolConfiguration threadPoolConfiguration) {
        checkArgument(threadPoolConfiguration);
        initializeThreadPool(threadPoolConfiguration.getCorePoolSize(), threadPoolConfiguration.getMaxPoolSize(), threadPoolConfiguration.getMaxAcceptCount(),
                threadPoolConfiguration.getRejectedExecutionHandler(), threadPoolConfiguration.getKeepAliveTime(), threadPoolConfiguration.getAllowCoreThreadTimeout());
    }

    private static void initializeThreadPool(Integer corePoolSize, Integer maxPoolSize, Integer maxAcceptCount, String rejectedExecutionHandler,
                                             Long keepAliveTime, Boolean allowCoreThreadTimeout) {

        if (!initialized.get()) {
            initialized.set(true);
            HandleMode handleMode = HandleMode.CALLERRUN;
            if (StringUtils.hasText(rejectedExecutionHandler)) {
                if (!HandleMode.REJECT.toString().equals(rejectedExecutionHandler) && !HandleMode.CALLERRUN.toString().equals(rejectedExecutionHandler)) {
                    throw new IllegalArgumentException("Invalid configuration properties async.rejectedExecutionHandler");
                }
                if (HandleMode.REJECT.toString().equals(rejectedExecutionHandler)) {
                    handleMode = HandleMode.REJECT;
                }
            }
            RejectedExecutionHandler handler = getRejectedHandler(handleMode);
            BlockingQueue<Runnable> queue = createQueue(maxAcceptCount);
            threadPool = new AsyncTaskThreadPool(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, queue, handler, new NamedThreadFactory());
            threadPool.getThreadPoolExecutor().allowCoreThreadTimeOut(allowCoreThreadTimeout);
            logger.info("ThreadPoolExecutor initialize info corePoolSize:{} maxPoolSize:{} maxAcceptCount:{} rejectedExecutionHandler:{}", corePoolSize, maxPoolSize, maxAcceptCount, handleMode);
        }
    }


    public static <T> void execute(AsyncCallable<T> task) {
        submit(task);
    }

    public static <T> com.woter.fact.async.core.AsyncFutureTask<T> submit(com.woter.fact.async.core.AsyncFutureCallable<T> callable) {
        return submit(callable, null);
    }

    public static <T> com.woter.fact.async.core.AsyncFutureTask<T> submit(com.woter.fact.async.core.AsyncFutureCallable<T> callable, com.woter.fact.async.core.AsyncFutureCallback<T> callback) {
        if (!initialized.get()) {
            AsyncConfigurer asyncConfigurer = new DefaultAsyncConfigurer();
            ThreadPoolConfiguration threadPoolConfiguration = new ThreadPoolConfiguration();
            asyncConfigurer.configureThreadPool(threadPoolConfiguration);
            initializeThreadPool(threadPoolConfiguration);
        }
        AsyncMethod method = buildAsyncMethod(callable);
        if (callable instanceof com.woter.fact.async.core.TransactionCallable) {
            callable = executeTransaction(callable);
        }
        return threadPool.submit(callable, callback, method);
    }

    public static void destroy() throws Exception {
        if (initialized.get() && (threadPool != null)) {
            threadPool.destroy();
            threadPool = null;
        }
    }

    public static <T> AsyncCallable<T> executeTransaction(final com.woter.fact.async.core.AsyncFutureCallable<T> callable) {
        if (transactionBuilder == null) {
            throw new AsyncException("you should integration spring transaction");
        }
        return new AsyncCallable<T>() {
            @Override
            public T doAsync() {
                return transactionBuilder.execute(callable);
            }
        };
    }

    private static <T> AsyncMethod buildAsyncMethod(com.woter.fact.async.core.AsyncFutureCallable<T> callable) {
        if (callable.cacheKey() != null) {
            AsyncMethod method = AsyncProxyCache.getAsyncMethod(callable.cacheKey());
            if (method != null) {
                return method;
            }
        }
        AsyncMethod method = new AsyncMethod(null, null, callable.timeout(), new AsyncRetry(callable.maxAttemps(), callable.exceptions()));
        Class<?> returnClass = ReflectionHelper.getGenericClass(callable.getClass());
        if (Void.TYPE.isAssignableFrom(returnClass) || Void.class.equals(returnClass)) {
            method.setVoid(true);
        }
        return method;
    }


    private static BlockingQueue<Runnable> createQueue(int acceptCount) {
        if (acceptCount > 0) {
            return new LinkedBlockingQueue<Runnable>(acceptCount);
        } else {
            return new SynchronousQueue<Runnable>();
        }
    }

    private static RejectedExecutionHandler getRejectedHandler(HandleMode mode) {
        return HandleMode.REJECT == mode ? new ThreadPoolExecutor.AbortPolicy() : new ThreadPoolExecutor.CallerRunsPolicy();
    }

    public static boolean isDestroyed() {
        return destroyed.get();
    }

    public static void setIsDestroyed(boolean isDestroyed) {
        AsyncExecutor.destroyed.set(true);
    }

    public static void setTransactionBuilder(TransactionBuilder transactionBuilder) {
        AsyncExecutor.transactionBuilder = transactionBuilder;
    }

    public static void setRunnableAround(RunnableAround runnableAround) {
        if (threadPool != null) {
            threadPool.setRunnableAround(runnableAround);
        }
    }

    public static AsyncTaskThreadPool getAsyncTaskThreadPool() {
        return threadPool;
    }

}
