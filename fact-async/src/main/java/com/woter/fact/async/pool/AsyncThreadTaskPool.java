package com.woter.fact.async.pool; 

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woter.fact.async.bean.SyncResult;
import com.woter.fact.async.core.AsyncFutureCallback;
import com.woter.fact.async.core.AsyncProfiler;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-3-23 下午1:57:34
 * @version      
 */
public class AsyncThreadTaskPool {
    
    private static Logger logger = LoggerFactory.getLogger(AsyncThreadTaskPool.class);
    
    private ThreadPoolExecutor threadPoolExecutor = null;
    
    public AsyncThreadTaskPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue){
	threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    
    public AsyncThreadTaskPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler){
	threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,handler);
    }
    
    public AsyncThreadTaskPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler,ThreadFactory threadFactory){
	threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory,handler);
    }
    
    
    public <T> AsyncFutureTask<T> submit(AsyncPoolCallable<T> task,AsyncFutureCallback<T> callback) {
        if (task == null) throw new NullPointerException();
        AsyncFutureTask<T> ftask = new AsyncFutureTask<T>(task,callback);
        if(AsyncProfiler.get().getNumber() >= threadPoolExecutor.getCorePoolSize()){
            T t = null;
            SyncResult<T> sync = new SyncResult<T>();
            try {
        	t = task.call();
        	sync.setValue(t);
        	if(callback != null){
        	    callback.onSuccess(t);
        	}
            } catch (Throwable e) {
        	sync.setThrowable(e);
        	if(callback != null){
        	    callback.onFailure(e);
        	}
            }
            ftask.setSync(sync);
            return ftask;
        }
        execute(ftask);
        return ftask;
    }
    
    public void execute(Runnable command) {
	threadPoolExecutor.execute(command);
    }
    
    public void destroy(){
	if(!threadPoolExecutor.isShutdown()){
	    threadPoolExecutor.shutdown();
	    logger.info("AsyncThreadTaskPool destroy;ThreadPoolExecutor Info:{}", threadPoolExecutor.toString());
	    threadPoolExecutor = null;
	}
    }
    
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
 