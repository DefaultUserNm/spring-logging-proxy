package ru.home.logging.util;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import static ru.home.logging.util.ObjectClassUtil.getConstructor;
import static ru.home.logging.util.ObjectClassUtil.getConstructorFields;

/*
 * @created 15.04.2023
 * @author alexander
 */
@RequiredArgsConstructor
class CGLibProxyFactory implements ProxyFactory {

    private final Object bean;
    private final Class<?> originalClass;
    private final MethodInterceptor interceptor;
    private final MethodSelector methodSelector;

    @Override
    public Object createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(originalClass);
        enhancer.setCallback(buildMethodInterceptor());
        Class<?>[] argumentTypes = getConstructor(bean).getParameterTypes();
        Object[] constructorFields = getConstructorFields(bean, originalClass, argumentTypes);

        return enhancer.create(argumentTypes, constructorFields);
    }

    private MethodInterceptor buildMethodInterceptor() {
        return (obj, method, args, proxy) -> {
            if (methodSelector.matches(method)) {
                return interceptor.intercept(bean, method, args, proxy);
            }
            return method.invoke(bean, args);
        };
    }
}
