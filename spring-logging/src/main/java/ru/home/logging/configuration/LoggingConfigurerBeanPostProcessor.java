package ru.home.logging.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import ru.home.logging.annotation.Logged;
import ru.home.logging.model.LoggedClassData;
import ru.home.logging.model.ProxyMode;
import ru.home.logging.util.ProxyModeResolver;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static ru.home.logging.model.ProxyMode.DEFAULT;
import static ru.home.logging.util.LoggingProxyFactory.createProxy;

/*
 * @created 13.04.2023
 * @author alexander
 */
@Slf4j
public class LoggingConfigurerBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, LoggedClassData> beanNamesMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.trace("Post processing bean '{}' - {} before initialization",
                beanName, bean.getClass().getCanonicalName());

        Logged classAnnotation = bean.getClass().getAnnotation(Logged.class);
        if (classAnnotation != null) {
            storeClass(bean, beanName, classAnnotation);
        } else {
            Set<Method> methods = extractLoggedMethods(bean);
            if (!methods.isEmpty()) {
                storeClass(bean, beanName, methods);
            }
        }
        log.trace("Finished post processing bean '{}' - {} before initialization",
                beanName, bean.getClass().getCanonicalName());

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.trace("Post processing bean '{}' - {} after initialization",
                beanName, bean.getClass().getCanonicalName());

        Object result = bean;
        LoggedClassData data = beanNamesMap.get(beanName);
        if (data != null) {
            log.debug("Creating logging proxy for bean '{}' - {}", beanName, bean.getClass().getCanonicalName());
            result = createProxy(bean, data);
        }
        log.trace("Finished post processing bean '{}' - {} after initialization",
                beanName, bean.getClass().getCanonicalName());

        return result;
    }

    /**
     * Save class to storage for future proxying
     * @param bean spring bean currently being processed
     * @param beanName spring bean name
     * @param classAnnotation @Logged annotation
     */
    private void storeClass(Object bean, String beanName, Logged classAnnotation) {
        log.debug(
                "Adding bean '{}' - {} to logged classes storage",
                beanName,
                bean.getClass().getCanonicalName()
        );
        LoggedClassData data = LoggedClassData.builder()
                .mode(ProxyModeResolver.getProxyMode(classAnnotation))
                .originalClass(bean.getClass())
                .build();
        beanNamesMap.put(beanName, data);
    }

    /**
     * Save class with specified methods to storage for future proxying
     * @param bean spring bean currently being processed
     * @param beanName spring bean name
     * @param methods methods which will be logged by proxy
     */
    private void storeClass(Object bean, String beanName, Set<Method> methods) {
        log.debug(
                "Adding bean '{}' - {} to logged classes storage with methods {}",
                beanName,
                bean.getClass().getCanonicalName(),
                methods
        );
        LoggedClassData data = LoggedClassData.builder()
                .mode(resolveProxyMode(methods))
                .originalClass(bean.getClass())
                .methods(methods)
                .build();
        beanNamesMap.put(beanName, data);
    }

    /**
     * Get methods which must be logged by proxy
     * @param bean spring bean currently being processed
     * @return methods which must be logged by proxy
     */
    private Set<Method> extractLoggedMethods(Object bean) {
        Set<Method> methods = new HashSet<>();
        for (Method method : extractMethods(bean)) {
            if (method.isAnnotationPresent(Logged.class)) {
                log.debug(
                        "Method {} in class {} has @Logged annotation",
                        method,
                        method.getDeclaringClass()
                );
                methods.add(method);
            }
        }

        return methods;
    }

    /**
     * Get methods declared in current bean class or its superclasses
     * @param bean spring bean currently being processed
     * @return methods
     */
    private Set<Method> extractMethods(Object bean) {
        Set<Method> result = new HashSet<>();
        Class<?> current = bean.getClass();
        while (current != Object.class) {
            result.addAll(Arrays.asList(current.getDeclaredMethods()));
            current = current.getSuperclass();
        }

        return result;
    }

    /**
     * Resolve proxy mode for class which has method-level @Logged annotation only
     * @param methods methods which must be logged by proxy
     * @return class proxy mode
     */
    private ProxyMode resolveProxyMode(Set<Method> methods) {
        ProxyMode proxyMode = DEFAULT;
        for (Method method : methods) {
            Logged annotation = method.getAnnotation(Logged.class);
            ProxyMode currentMode = ProxyModeResolver.getProxyMode(annotation);
            if (proxyMode != DEFAULT && currentMode != DEFAULT) {
                if (proxyMode != currentMode) {
                    log.warn(
                            "Method {} in class {} must have the same proxyMode in Logged annotation as " +
                            "the other methods. Will use {} as it was found earlier",
                            method,
                            method.getDeclaringClass(),
                            proxyMode
                    );
                }
            } else {
                proxyMode = currentMode;
            }
        }
        return proxyMode;
    }
}