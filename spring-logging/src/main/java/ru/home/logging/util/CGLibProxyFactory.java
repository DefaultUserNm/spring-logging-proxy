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
public class CGLibProxyFactory implements ProxyFactory {

    private final Object bean;
    private final Class<?> originalClass;
    private final MethodInterceptor interceptor;

    @Override
    public Object createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(originalClass);
        enhancer.setCallback(interceptor);
        Class<?>[] argumentTypes = getConstructor(bean).getParameterTypes();
        Object[] constructorFields = getConstructorFields(bean, originalClass, argumentTypes);

        return enhancer.create(argumentTypes, constructorFields);
    }
}
