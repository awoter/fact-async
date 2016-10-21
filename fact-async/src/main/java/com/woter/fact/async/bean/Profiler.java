package com.woter.fact.async.bean; 

import java.util.concurrent.atomic.AtomicInteger;




/**
 *  
 * @author	hz15041240 
 * @date	2016-8-18 下午5:53:06
 * @version     
 */
public class Profiler {
    
    private AtomicInteger number ;
    
    public Profiler(){
	this.number = new AtomicInteger();
    };
    
    public Profiler(int number){
	this.number = new AtomicInteger();
	this.number.set(number);
    }
    
    public Profiler getAndIncrement(){
	number.getAndIncrement();
	return this;
    }
    
    public Profiler decrementAndGet(){
	number.decrementAndGet();
	return this;
    }
    
    
    public int getNumber() {
        return this.number.get();
    }

    
    public void setNumber(int number) {
        this.number.set(number);
    }
    
}
 