package com.woter.fact.async.core;


/**
 * @author woter
 * @date 2016-8-18 下午5:19:07
 */
public class AsyncCounter {

    private static ThreadLocal<Integer> counterThreadMap = new ThreadLocal<Integer>();

    public static int intValue() {
        Integer counter = counterThreadMap.get();
        if (counter == null) {
            counter = 0;
        }
        return counter;
    }

    public static Integer get() {
        return counterThreadMap.get();
    }

    public static void set(Integer counter) {
        if (counter == null) {
            counter = 0;
        }
        counterThreadMap.set(counter);
    }

    public static void release() {
        counterThreadMap.remove();
    }
}
 