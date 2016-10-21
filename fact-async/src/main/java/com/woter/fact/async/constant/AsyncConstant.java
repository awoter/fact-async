package com.woter.fact.async.constant; 

/**
 * <p>
 * 
 * 
 *
 * </p>
 * @author	woter 
 * @date	2016-3-23 下午2:32:44
 * @version      
 */
public class AsyncConstant {
    
    public static final String ASYNC_DEFAULT_THREAD_NAME = "Async-Pool"; //线程名称
    public static final long ASYNC_DEFAULT_TIME_OUT= 0; //默认执行任务超时时间-单位毫秒（0表示不限制超时）
    public static final long ASYNC_DEFAULT_SCAN_TIME_OUT= -1; //默认执行任务超时时间-单位毫秒（0表示不限制超时）
    public static final long ASYNC_DEFAULT_KEEPALIVETIME= 10000l; //默认线程空闲超时时间
    public static boolean ASYNC_DEFAULT_TRACE_LOG = false; //默认跟踪日志关闭
}
 