package com.woter.fact.async.annotation; 

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.woter.fact.async.constant.AsyncConstant;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-3-23 上午10:41:51
 * @version      
 */
@Target({ TYPE, FIELD, METHOD })
@Retention(RUNTIME)
public @interface Async {
    
    /**
     * 
     * <p>
     * 
     * 调用超时设置-单位毫秒(默认0-不超时)
     *
     * </p>
     * @return
     *  
     * @author	woter 
     * @date	2016-3-23 上午10:53:11
     * @version
     */
    public long timeout() default AsyncConstant.ASYNC_DEFAULT_TIME_OUT;
    
}
 