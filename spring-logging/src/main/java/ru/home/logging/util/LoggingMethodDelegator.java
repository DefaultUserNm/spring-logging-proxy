package ru.home.logging.util;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static ru.home.logging.util.ProxyInvoker.invokeWithLogging;
import static ru.home.logging.util.ProxyLogger.logAfter;
import static ru.home.logging.util.ProxyLogger.logAfterThrowing;
import static ru.home.logging.util.ProxyLogger.logBefore;

/*
 * @created 22.04.2023
 * @author alexander
 */
@RequiredArgsConstructor
public class LoggingMethodDelegator {

    private final Object bean;

    @RuntimeType
    public Object intercept(
            @Origin Method method,
            @Origin Class<?> originalClass,
            @AllArguments Object[] args) throws Exception {
        return invokeWithLogging(bean, originalClass, method, args);
    }
}
