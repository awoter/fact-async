package com.woter.fact.async.util;

/**
 * <p>
 * 
 * 
 * 
 * </p>
 * 
 * @author woter
 * @date 2015-9-9 上午11:51:44
 * @version
 */
public class ValidationUtils {

	public static void checkNotNull(Object ref) {
		if (ref != null) {
			return;
		}
		throw new NullPointerException();
	}

	public static void checkNotNull(Object ref, String message) {
		if (ref != null) {
			return;
		}
		throw new NullPointerException(message);
	}

	public static void checkState(boolean exps) {
		if (exps) {
			return;
		}
		throw new IllegalStateException();
	}

	public static void checkState(boolean exps, String message) {
		if (exps) {
			return;
		}
		throw new IllegalStateException(message);
	}

}
