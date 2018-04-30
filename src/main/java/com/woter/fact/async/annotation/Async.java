package com.woter.fact.async.annotation;

import com.woter.fact.async.constant.AsyncConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-3-23 上午10:41:51
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
public @interface Async {

    /**
     * <p>
     * <p>
     * 调用超时设置-单位毫秒(默认0-不超时)
     *
     * </p>
     *
     * @return
     * @author woter
     * @date 2016-3-23 上午10:53:11
     * @version
     */
    long timeout() default AsyncConstant.ASYNC_DEFAULT_TIME_OUT;


    /**
     * <p>
     * <p>
     * 异步调用异常了最多重试次数
     *
     * </p>
     *
     * @return
     * @author woter
     * @date 2016-12-9 上午9:19:10
     * @version
     */
    int maxAttemps() default AsyncConstant.ASYNC_DEFAULT_RETRY;


    /***
     *
     * @return
     *
     * @author woter
     * @date 2016-12-9 上午11:23:46
     * @version
     */
    Class<? extends Throwable>[] exceptions() default Throwable.class;

}
 