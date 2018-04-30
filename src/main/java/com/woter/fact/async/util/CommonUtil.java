package com.woter.fact.async.util;

import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * <p>
 *
 *
 *
 * </p>
 *
 * @author woter
 * @date 2016-4-5 下午5:31:28
 */
public final class CommonUtil {

    public static Class<?> getClass(Object object) {
        boolean isCglibProxy = false;
        if (AopUtils.isCglibProxy(object)) {
            isCglibProxy = true;
        }
        if (isCglibProxy || ClassUtils.isCglibProxy(object)) {
            isCglibProxy = true;
        }
        Class<?> targetClass = object.getClass();
        if (isCglibProxy) {
            targetClass = targetClass.getSuperclass();
        }
        return targetClass;
    }

    public static String buildkey(Object object, Method method) {
        ValidationUtils.checkNotNull(object);
        return new StringBuilder(getClass(object).getName()).append(".").append(buildMethod(method)).toString();
    }

    public static String buildkey(String name, boolean b) {
        ValidationUtils.checkNotNull(name);
        return new StringBuilder(name).append("#").append(b).toString();
    }

    public static String buildMethod(Method method) {
        StringBuilder strbuilder = new StringBuilder(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 0) {
            for (Class<?> parameterType : parameterTypes) {
                strbuilder.append("#").append(parameterType.getName());
            }
        }
        return strbuilder.toString();
    }

    /**
     * <p>
     * <p>
     * 校验是否只包含 数字0-9 是 返回true 否则返回false
     * </p>
     *
     * @param number
     * @return
     * @author woter
     * @date 2016-4-8 下午3:37:27
     * @version
     */
    public static boolean isNumber(String number) {
        String regex = "^[0-9]+$";
        return Pattern.matches(regex, number);
    }
}
