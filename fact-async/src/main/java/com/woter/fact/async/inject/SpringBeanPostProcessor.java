package com.woter.fact.async.inject; 

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-4-1 下午4:39:30
 * @version      
 */
public abstract class SpringBeanPostProcessor implements BeanPostProcessor,DisposableBean,InitializingBean{
    
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
	return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
	return processAasynBean(bean,beanName,true);
    }
    
    public abstract Object processAasynBean(Object bean, String beanName,boolean supportTransactional);
    
}
 