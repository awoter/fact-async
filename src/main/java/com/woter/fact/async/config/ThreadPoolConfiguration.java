package com.woter.fact.async.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author woter
 * @date 2017-12-4 下午12:35:49
 */
public class ThreadPoolConfiguration {

    @Value("${async.corePoolSize:}")
    private Integer corePoolSize;

    @Value("${async.maxPoolSize:}")
    private Integer maxPoolSize;

    @Value("${async.maxAcceptCount:}")
    private Integer maxAcceptCount;

    @Value("${async.rejectedExecutionHandler:CALLERRUN}")
    private String rejectedExecutionHandler;

    @Value("${async.allowCoreThreadTimeout:true}")
    private Boolean allowCoreThreadTimeout;

    @Value("${async.keepAliveTime:}")
    private Long keepAliveTime;

    public Integer getCorePoolSize() {
        return corePoolSize;
    }


    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }


    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }


    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }


    public Integer getMaxAcceptCount() {
        return maxAcceptCount;
    }


    public void setMaxAcceptCount(Integer maxAcceptCount) {
        this.maxAcceptCount = maxAcceptCount;
    }


    public String getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }


    public void setRejectedExecutionHandler(String rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }


    public Boolean getAllowCoreThreadTimeout() {
        return allowCoreThreadTimeout;
    }


    public void setAllowCoreThreadTimeout(Boolean allowCoreThreadTimeout) {
        this.allowCoreThreadTimeout = allowCoreThreadTimeout;
    }


    public Long getKeepAliveTime() {
        return keepAliveTime;
    }


    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

}
 