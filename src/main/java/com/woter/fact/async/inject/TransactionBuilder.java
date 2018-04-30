package com.woter.fact.async.inject;

import com.woter.fact.async.core.AsyncFutureCallable;
import com.woter.fact.async.exception.AsyncException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author woter
 * @date 2016-12-20 上午10:36:58
 */
public class TransactionBuilder {

    @Transactional(rollbackFor = Exception.class)
    public <T> T execute(AsyncFutureCallable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new AsyncException(e);
        }
    }

}
