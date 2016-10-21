package com.woter.fact.async.pool; 

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.woter.fact.async.constant.AsyncConstant;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-3-23 下午2:55:23
 * @version      
 */
public class NamedThreadFactory implements ThreadFactory{
    
    final private String        name;
    final private boolean       daemon;
    final private ThreadGroup   group;
    final private AtomicInteger threadNumber = new AtomicInteger(1);

    public NamedThreadFactory(){
        this(AsyncConstant.ASYNC_DEFAULT_THREAD_NAME, true);
    }

    public NamedThreadFactory(String name, boolean daemon){
        this.name = name;
        this.daemon = daemon;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, name + "-" + threadNumber.getAndIncrement(), 0);
        t.setDaemon(daemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
 