package com.woter.fact.async.exception; 

/**
 * <p>
 * 
 * 
 *
 * </p>
 * @author	woter 
 * @date	2016-3-23 下午7:50:44
 * @version      
 */
public class AsyncTimeoutException extends RuntimeException{
    
    private static final long serialVersionUID = -2128834565845654572L;

    public AsyncTimeoutException(){
        super();
    }

    public AsyncTimeoutException(String message, Throwable cause){
        super(message, cause);
    }

    public AsyncTimeoutException(String message){
        super(message);
    }

    public AsyncTimeoutException(Throwable cause){
        super(cause);
    }
    
}
 