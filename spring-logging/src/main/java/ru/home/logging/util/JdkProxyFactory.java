package ru.home.logging.util;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/*
 * @created 15.04.2023
 * @author alexander
 */
@RequiredArgsConstructor
class JdkProxyFactory implements ProxyFactory {

    private final Object bean;
    private final Class<?> originalClass;
    private final InvocationHandler handler;
    private final MethodSelector methodSelector;

    @Override
    public Object createProxy() {
        return Proxy.newProxyInstance(
                originalClass.getClassLoader(),
                originalClass.getInterfaces(),
                buildInvocationHandler()
        );
    }

    private InvocationHandler buildInvocationHandler() {
        return (proxy, method, args) -> {
            if (methodSelector.matches(method)) {
                return handler.invoke(bean, method, args);
            }
            return method.invoke(bean, args);
        };
    }
}