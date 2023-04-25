package ru.home.logging.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/*
 * @created 14.04.2023
 * @author alexander
 */
@Slf4j
public class ProxyLogger {
    static void logBefore(Class<?> originalClass, Method method, Object[] args) {
        log.info(
                "Invoking {}#{} with args {}",
                originalClass.getCanonicalName(),
                method.getName(),
                args == null ? new Object[]{} : args
        );
    }

    static void logAfter(Class<?> originalClass, Method method, Object result) {
        log.info(
                "Method {}#{} invocation result: {}",
                originalClass.getCanonicalName(),
                method.getName(),
                result
        );
    }

    static void logAfterThrowing(Class<?> originalClass, Method method, Exception ex) {
        log.info(
                "Method {}#{} invocation failed with exception: {}",
                originalClass.getCanonicalName(),
                method.getName(),
                getStackTrace(ex)
        );
    }
}
