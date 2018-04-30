package com.woter.fact.async.pool;

import com.woter.fact.async.bean.AsyncMethod;
import com.woter.fact.async.core.AsyncFutureCallback;
import com.woter.fact.async.core.AsyncFutureTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-23 下午1:57:34
 */
public final class AsyncTaskThreadPool {

    private static Logger logger = LoggerFactory.getLogger(AsyncTaskThreadPool.class);

    private ThreadPoolExecutor executor = null;

    private RunnableAround runnableAround;

    private int corePoolSize;

    public AsyncTaskThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                               BlockingQueue<Runnable> workQueue) {
        this.corePoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public AsyncTaskThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                               BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this.corePoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public AsyncTaskThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                               BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, ThreadFactory threadFactory) {
        this.corePoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    public <T> AsyncFutureTask<T> submit(Callable<T> callable, AsyncFutureCallback<T> callback, AsyncMethod method) {
        if (callable == null) throw new NullPointerException();
        AsyncFutureTask<T> futureTask = new AsyncFutureTask<T>(callable, callback, method);
        if (futureTask.getCounter() > 0 && corePoolSize <= executor.getActiveCount()) {
            futureTask.syncRun();
            return futureTask;
        }
        execute(futureTask);
        return futureTask;
    }

    private void execute(Runnable command) {
        if (runnableAround != null) {
            command = runnableAround.advice(command);
        }
        executor.execute(command);
    }

    public void destroy() throws Exception {
        if (!executor.isShutdown()) {
            executor.shutdown();
            boolean loop = true;
            do {
                loop = executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
                logger.info("Wait for the async thread to finish the work; The remaining queue size: {}", executor.getQueue().size());
            } while (!loop);
            logger.info("AsyncThreadTaskPool destroyed {}", executor.toString());
            executor = null;
        }
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return executor;
    }

    public void setRunnableAround(RunnableAround runnableAround) {
        this.runnableAround = runnableAround;
    }

}
 