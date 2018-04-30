package com.woter.fact.async.inject; 

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.woter.fact.async.constant.AsyncConstant;
import com.woter.fact.async.core.AsyncExecutor;
import com.woter.fact.async.template.AsyncTemplate;
import com.woter.fact.async.util.CommonUtil;
import com.woter.fact.async.util.ReflectionHelper;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-4-1 下午5:00:28
 * @version      
 */
@Component
public class AsyncSpringAOPSupport extends SpringBeanPostProcessor implements Ordered{
    
    @Value("${async.corePoolSize}")
    private String corePoolSize;
    
    @Value("${async.maxPoolSize}")
    private String maxPoolSize;
    
    @Value("${async.maxAcceptCount}")
    private String maxAcceptCount;
    
    @Value("${async.rejectedExecutionHandler}")
    private String rejectedExecutionHandler;
    
    @Value("${async.allowCoreThreadTimeout}")
    private String allowCoreThreadTimeout;
    
    @Value("${async.keepAliveTime}")
    private String keepAliveTime;
    
    @Value("${async.traced}")
    private String traced;
    
    public Object processAasynBean(Object bean, String beanName,boolean supportTransactional){
	
	Method[] methods = bean.getClass().getDeclaredMethods();
	if(methods == null || methods.length ==0){
	    return bean;
	}
	for (Method method : methods) {
	    if (ReflectionHelper.findAsyncAnnatation(bean, method) != null) {
		return AsyncTemplate.getAsyncProxy(AsyncTemplate.ProxyType.CGLIB).buildProxy(bean, AsyncConstant.ASYNC_DEFAULT_SCAN_TIME_OUT, false);
	    }
	}
	return bean;
    }
    
    public int getOrder(){
	return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void destroy() throws Exception {
	AsyncExecutor.setIsDestroyed(true);
	AsyncExecutor.destroy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	Integer corePoolSize = null;
	Integer maxPoolSize = null;
	Integer maxAcceptCount = null;
	Long keepAliveTime = AsyncConstant.ASYNC_DEFAULT_KEEPALIVETIME;
	Boolean allowCoreThreadTimeout = true;
	
	if(!StringUtils.isEmpty(this.corePoolSize) && CommonUtil.isNumber(this.corePoolSize)){
	    corePoolSize = Integer.valueOf(this.corePoolSize);
	}
	if(!StringUtils.isEmpty(this.maxPoolSize) && CommonUtil.isNumber(this.maxPoolSize)){
	    maxPoolSize = Integer.valueOf(this.maxPoolSize);
	}
	if(!StringUtils.isEmpty(this.maxAcceptCount) && CommonUtil.isNumber(this.maxAcceptCount)){
	    maxAcceptCount = Integer.valueOf(this.maxAcceptCount);
	}
	if(!StringUtils.isEmpty(this.keepAliveTime) && CommonUtil.isNumber(this.keepAliveTime)){
	    keepAliveTime = Long.valueOf(this.keepAliveTime);
	}
	if(!StringUtils.isEmpty(this.allowCoreThreadTimeout)){
	    allowCoreThreadTimeout = Boolean.parseBoolean(this.allowCoreThreadTimeout);
	}
	if(!StringUtils.isEmpty(this.traced)){
	    AsyncConstant.ASYNC_DEFAULT_TRACE_LOG = Boolean.parseBoolean(this.traced);
	}
	
	AsyncExecutor.initPool(corePoolSize, maxPoolSize, maxAcceptCount, rejectedExecutionHandler,keepAliveTime,allowCoreThreadTimeout);
    }

}
 