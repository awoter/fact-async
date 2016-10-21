package com.woter.fact.async.util; 

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	woter 
 * @date	2016-3-31 下午7:34:16
 * @version      
 */
public class DateUtils {
    
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(new Date());
	}
}
 