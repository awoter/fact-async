package com.woter.fact.async.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author woter
 * @date 2017-12-4 下午2:08:27
 */
public class ExecutorConfiguration {

    @Value("${async.traced:false}")
    private Boolean traced;


    public Boolean getTraced() {
        return traced;
    }


    public void setTraced(Boolean traced) {
        this.traced = traced;
    }


}
 