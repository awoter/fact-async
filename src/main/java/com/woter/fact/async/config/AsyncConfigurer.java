package com.woter.fact.async.config;


import com.woter.fact.async.pool.RunnableAround;


/**
 * @author woter
 * @date 2017-12-4 下午12:32:40
 */
public interface AsyncConfigurer {

    void configureExecutorConfiguration(com.woter.fact.async.config.ExecutorConfiguration configuration);

    void configureThreadPool(com.woter.fact.async.config.ThreadPoolConfiguration configuration);

    RunnableAround getRunnableAround();

}