package com.woter.fact.async.core; 

/**
 * 
 * <p>
 * 
 * 异步执行完成事件接口
 *
 * </p>
 *  
 * @author	hz15041240 
 * @date	2016-4-21 上午11:28:28
 * @version
 */
public interface AsyncFutureHandler<V> {
    
    /**
     * 
     * <p>
     * 
     * 代理调用完成
     *
     * </p>
     * @param isTimeout 是否超时 </br>
     * 		true：超时则v返回null</br>
     * 		false:V v则有结果
     * @param v
     *  
     * @author	hz15041240 
     * @date	2016-4-21 上午11:27:17
     * @version
     */
    public void processComplete(boolean isTimeout,V v);
    
}
 