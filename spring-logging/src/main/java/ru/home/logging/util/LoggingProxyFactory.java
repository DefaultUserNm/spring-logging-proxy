package ru.home.logging.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.home.logging.model.LoggedClassData;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static ru.home.logging.util.LoggingMethodDelegator.invokeWithLogging;
import static ru.home.logging.util.MethodUtil.calculateKey;

/*
 * @created 13.04.2023
 * @author alexander
 */
@Slf4j
@UtilityClass
public class LoggingProxyFactory {
    public static Object createProxy(Object bean, LoggedClassData data) {
        if (bean == null) {
            return null;
        }
        try {
            return switch (ProxyModeResolver.resolve(data)) {
                case JDK -> createJdkDynamicProxy(bean, data);
                case CGLIB -> createCGLibProxy(bean, data);
                default -> createByteBuddyProxy(bean, data);
            };
        } catch (Exception ex) {
            log.error("Failed to create proxy object for {}: {}", bean.getClass(), getStackTrace(ex));
            return bean;
        }
    }

    private Object createJdkDynamicProxy(Object bean, LoggedClassData data) {
        Set<String> methods = buildMethodKeys(data.getMethods());
        ProxyFactory proxyFactory = new JdkProxyFactory(
                bean,
                data.getOriginalClass(),
                (proxy, method, args) -> invokeWithLogging(bean, data.getClass(), method, args),
                methods.isEmpty()
                        ? method -> true
                        : method -> methods.contains(calculateKey(method))

        );

        return proxyFactory.createProxy();
    }

    private Object createCGLibProxy(Object bean, LoggedClassData data) {
        Set<String> methods = buildMethodKeys(data.getMethods());
        ProxyFactory proxyFactory = new CGLibProxyFactory(
                bean,
                data.getOriginalClass(),
                (obj, method, args, proxy) -> invokeWithLogging(bean, data.getClass(), method, args),
                methods.isEmpty()
                        ? method -> true
                        : method -> methods.contains(calculateKey(method))
        );

        return proxyFactory.createProxy();
    }

    private Object createByteBuddyProxy(Object bean, LoggedClassData data) {
        ProxyFactory proxyFactory = new ByteBuddyProxyFactory(
                bean,
                data.getOriginalClass(),
                new LoggingMethodDelegator(bean),
                data.getMethods()
        );

        return proxyFactory.createProxy();
    }

    private Set<String> buildMethodKeys(Set<Method> methods) {
        return Optional.ofNullable(methods)
                .orElseGet(Collections::emptySet)
                .stream()
                .map(MethodUtil::calculateKey)
                .collect(Collectors.toSet());
    }
}
