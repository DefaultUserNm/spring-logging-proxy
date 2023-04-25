package ru.home.logging.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static ru.home.logging.util.ProxyLogger.logAfter;
import static ru.home.logging.util.ProxyLogger.logAfterThrowing;
import static ru.home.logging.util.ProxyLogger.logBefore;

/*
 * @created 26.04.2023
 * @author alexander
 */
@UtilityClass
public class ProxyInvoker {
    public static Object invokeWithLogging(
            Object bean,
            Class<?> originalClass,
            Method method,
            Object[] args) throws InvocationTargetException, IllegalAccessException {
        logBefore(originalClass, method, args);
        try {
            Object result = method.invoke(bean, args);
            logAfter(originalClass, method, result);
            return result;
        } catch (Exception ex) {
            logAfterThrowing(originalClass, method, ex);
            throw ex;
        }
    }
}
