/**
 * Copyright (c) 2006-2015 Hzins Ltd. All Rights Reserved.
 * <p>
 * This code is the confidential and proprietary information of
 * Hzins. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Hzins,http://www.hzins.com.
 */
package com.woter.fact.async.constant;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-23 下午2:32:44
 */
public final class AsyncConstant {

    public static final String ASYNC_DEFAULT_THREAD_NAME = "Async-Pool"; //线程名称
    public static final long ASYNC_DEFAULT_TIME_OUT = 0; //默认执行任务超时时间-单位毫秒（0表示不限制超时）
    public static final long ASYNC_DEFAULT_KEEPALIVETIME = 60000l; //默认线程空闲超时时间
    public static boolean ASYNC_DEFAULT_TRACE_LOG = false; //默认跟踪日志关闭 
    public static final int ASYNC_DEFAULT_RETRY = 0;  //默认重试次数
}
 