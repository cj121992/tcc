package com.melot.common.transaction.utils;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.melot.common.transaction.annotation.ClassTransactional;
import com.melot.common.transaction.annotation.LogTransactional;
import com.melot.common.transaction.annotation.TccTransactional;

public class MethodAnnotationUtil {

	public static Method getAnnotationedMethod(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) (pjp.getSignature())).getMethod();

        if ((method.getAnnotation(TccTransactional.class) == null) &&
        		(method.getAnnotation(LogTransactional.class) == null) &&
        		(method.getAnnotation(ClassTransactional.class) == null)) {
            try {
                method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return method;
    }
}
