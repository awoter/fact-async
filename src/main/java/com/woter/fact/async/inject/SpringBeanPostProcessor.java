package com.woter.fact.async.inject;

import com.woter.fact.async.annotation.Async;
import com.woter.fact.async.constant.AsyncConstant;
import com.woter.fact.async.core.AsyncExecutor;
import com.woter.fact.async.template.AsyncTemplate;
import com.woter.fact.async.util.CommonUtil;
import com.woter.fact.async.util.ReflectionHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-4-1 下午4:39:30
 */
public class SpringBeanPostProcessor implements BeanPostProcessor, Ordered {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return processAasynBean(bean, beanName);
    }

    public Object processAasynBean(Object bean, String beanName) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return bean;
        }
        for (Method method : methods) {
            Async annotation = ReflectionHelper.findAsyncAnnatation(bean, method);
            if (annotation != null) {
                return AsyncTemplate.getAsyncProxy(AsyncTemplate.ProxyType.CGLIB).buildProxy(bean, AsyncConstant.ASYNC_DEFAULT_TIME_OUT, false);
            }
        }
        if (CommonUtil.getClass(bean).isAssignableFrom(TransactionBuilder.class)) {
            AsyncExecutor.setTransactionBuilder((TransactionBuilder) bean);
        }
        return bean;

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
 