package com.woter.fact.async.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.woter.fact.async.constant.AsyncConstant;
import com.woter.fact.async.pool.AsyncFutureTask;
import com.woter.fact.async.pool.AsyncPoolCallable;
import com.woter.fact.async.pool.AsyncThreadTaskPool;
import com.woter.fact.async.pool.NamedThreadFactory;

/**
 * <p>
 * 
 * 异步线程执行器
 * 
 * </p>
 * 
 * @author hz15041240
 * @date 2016-3-23 上午11:22:17
 * @version
 */
public class AsyncExecutor {

    private final static Logger logger = LoggerFactory.getLogger(AsyncExecutor.class);

    private static AsyncThreadTaskPool pool;

    private static AtomicBoolean isInit = new AtomicBoolean(false);
    private static AtomicBoolean isDestroyed = new AtomicBoolean(false);
    
    enum HandleMode {
	REJECT, CALLERRUN;
    }

    public static void initPool(Integer corePoolSize, Integer maxPoolSize, Integer maxAcceptCount, String rejectedExecutionHandler, Long keepAliveTime, Boolean allowCoreThreadTimeout) {

	if (!isInit.get()) {
	    isInit.set(true);
	    if (corePoolSize == null || corePoolSize <= 0) {
		corePoolSize = Runtime.getRuntime().availableProcessors() * 4;
	    }
	    if (maxPoolSize == null || maxPoolSize <= 0) {
		maxPoolSize = corePoolSize;
	    }
	    if (maxAcceptCount == null || maxAcceptCount < 0) {
		maxAcceptCount = (corePoolSize / 2);
	    }
	    HandleMode handleMode = HandleMode.CALLERRUN;
	    if (!StringUtils.isEmpty(rejectedExecutionHandler)) {
		if ("REJECT".equals(rejectedExecutionHandler)) {
		    handleMode = HandleMode.REJECT;
		}
	    }
	    if (keepAliveTime == null || keepAliveTime < 0) {
		keepAliveTime = AsyncConstant.ASYNC_DEFAULT_KEEPALIVETIME;
	    }
	    if (allowCoreThreadTimeout == null) {
		allowCoreThreadTimeout = true;
	    }

	    RejectedExecutionHandler handler = getRejectedHandler(handleMode);
	    BlockingQueue<Runnable> queue = createQueue(maxAcceptCount);
	    pool = new AsyncThreadTaskPool(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, queue, handler, new NamedThreadFactory());
	    pool.getThreadPoolExecutor().allowCoreThreadTimeOut(allowCoreThreadTimeout);
	    logger.info("ThreadPoolExecutor initialize info corePoolSize:{} maxPoolSize:{} maxAcceptCount:{} rejectedExecutionHandler:{}", corePoolSize, maxPoolSize, maxAcceptCount, handleMode);
	}
    }

    public static <T> AsyncFutureTask<T> submit(AsyncPoolCallable<T> task) {
	return submit(task,null);
    }

    public static <T> AsyncFutureTask<T> submit(AsyncPoolCallable<T> task, AsyncFutureCallback<T> callback) {
	if (!isInit.get()) {
	    initPool(null, null, null, null, null, null);
	}
	return pool.submit(task, callback);
    }

    /*public static void submit(AsyncRunnable runnable) {
	if (!isInit.get()) {
	    logger.warn("project started not initialize pluto-async");
	    initPool(null, null, null, null, null, null);
	}
	if(isDestroyed()){
	    runnable.run();
	}else{
	    pool.execute(runnable);
	}
    }*/

    public static void destroy() {
	if (isInit.get() && (pool != null)) {
	    pool.destroy();
	    pool = null;
	}
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
        return isDestroyed.get();
    }

    public static void setIsDestroyed(boolean isDestroyed) {
	AsyncExecutor.isDestroyed.set(true);
    }
}
