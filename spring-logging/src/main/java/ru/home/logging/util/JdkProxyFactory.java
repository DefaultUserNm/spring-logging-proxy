package ru.home.logging.util;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/*
 * @created 15.04.2023
 * @author alexander
 */
@RequiredArgsConstructor
public class JdkProxyFactory implements ProxyFactory {

    private final Class<?> originalClass;
    private final InvocationHandler handler;

    @Override
    public Object createProxy() {
        return Proxy.newProxyInstance(
                originalClass.getClassLoader(),
                originalClass.getInterfaces(),
                handler
        );
    }
}