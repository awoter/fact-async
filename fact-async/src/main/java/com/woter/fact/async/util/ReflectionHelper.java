package com.woter.fact.async.util; 

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.woter.fact.async.annotation.Async;

/**
 * <p>
 * 
 *
 *
 * </p>
 * @author	hz15041240 
 * @date	2016-4-11 下午2:37:14
 * @version      
 */
@SuppressWarnings("all")
public class ReflectionHelper {
    
    private static final Map primitiveValueMap = new HashMap(16);
    static {
        primitiveValueMap.put(Boolean.class, Boolean.FALSE);
        primitiveValueMap.put(Byte.class, Byte.valueOf((byte) 0));
        primitiveValueMap.put(Character.class, Character.valueOf((char) 0));
        primitiveValueMap.put(Short.class, Short.valueOf((short) 0));
        primitiveValueMap.put(Double.class, Double.valueOf(0));
        primitiveValueMap.put(Float.class, Float.valueOf(0));
        primitiveValueMap.put(Integer.class, Integer.valueOf(0));
        primitiveValueMap.put(Long.class, Long.valueOf(0));
        primitiveValueMap.put(boolean.class, Boolean.FALSE);
        primitiveValueMap.put(byte.class, Byte.valueOf((byte) 0));
        primitiveValueMap.put(char.class, Character.valueOf((char) 0));
        primitiveValueMap.put(short.class, Short.valueOf((short) 0));
        primitiveValueMap.put(double.class, Double.valueOf(0));
        primitiveValueMap.put(float.class, Float.valueOf(0));
        primitiveValueMap.put(int.class, Integer.valueOf(0));
        primitiveValueMap.put(long.class, Long.valueOf(0));

    }
    
    public static Object newInstance(Class type) {
        Constructor constructor = null;
        Object[] constructorArgs = new Object[0];
        try {
            constructor = type.getConstructor(new Class[] {});// 先尝试默认的空构造函数
        } catch (NoSuchMethodException e) {
            // ignore
        }

        if (constructor == null) {// 没有默认的构造函数，尝试别的带参数的函数
            Constructor[] constructors = type.getConstructors();
            if (constructors == null || constructors.length == 0) {
                throw new UnsupportedOperationException("Class[" + type.getName() + "] has no public constructors");
            }
            constructor = constructors[getSimpleParamenterTypeIndex(constructors)];// 默认取第一个参数
            Class[] params = constructor.getParameterTypes();
            constructorArgs = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                constructorArgs[i] = getDefaultValue(params[i]);
            }
        }
        
        return ReflectUtils.newInstance(constructor, constructorArgs);
    }
    
    public static int getSimpleParamenterTypeIndex(Constructor[] constructors){
	Constructor constructor = null;
	Class[] params = null;
	boolean isSimpleTypes;
	for(int i= 0;i < constructors.length ;i++){
	    constructor = constructors[i];
	    params = constructor.getParameterTypes();
	    if(params.length >0){
		isSimpleTypes = true;
		for(int j = 0;j <params.length; j++){
		    if(primitiveValueMap.get(params[j]) ==null){
			isSimpleTypes = false;
			break;
		    }
		}
		if(isSimpleTypes){
		    return i;
		}
	    }else{
		return i;
	    }
	}
	return 0;
    }
    
    public static Object getDefaultValue(Class cl) {
        if (cl.isArray()) {// 处理数组
            return Array.newInstance(cl.getComponentType(), 0);
        } else if (cl.isPrimitive() || primitiveValueMap.containsKey(cl)) { // 处理原型
            return primitiveValueMap.get(cl);
        } else {
            return null;
        }
    }
    
    public static Object invoke(Object finObj,Object[] finArgs,Method method) throws Throwable{
	try {
	    return method.invoke(finObj, finArgs);
        } catch (IllegalAccessException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }
    
    public static Class<?> getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class<?>) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
        } else {
            return (Class<?>) genericClass;
        }
    }
    
    public static Async findAsyncAnnatation(Object bean, Method method) {
	Async classAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), Async.class);
	Async methodAnnotation = AnnotationUtils.findAnnotation(method, Async.class);
	if (methodAnnotation != null || classAnnotation != null) {
	    Class<?> returnClass = method.getReturnType();
	    if (Void.TYPE.isAssignableFrom(returnClass) || (Modifier.isPublic(returnClass.getModifiers()) && !Modifier.isFinal(returnClass.getModifiers()) 
		    && !(returnClass.isPrimitive() || returnClass.isArray()) && returnClass != Object.class)) {
		if (methodAnnotation != null) {
		    return methodAnnotation;
		}
		return classAnnotation;
	    }
	}
	return null;
    }
}
 