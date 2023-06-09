package ru.home.logging.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatcher.Junction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.isFinal;
import static net.bytebuddy.matcher.ElementMatchers.isPrivate;
import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;
import static ru.home.logging.util.ObjectClassUtil.chooseConstructor;
import static ru.home.logging.util.ObjectClassUtil.getConstructorFields;

/*
 * @created 21.04.2023
 * @author alexander
 */
@RequiredArgsConstructor
class ByteBuddyProxyFactory<T> implements ProxyFactory<T> {

    private final T bean;
    private final Class<?> originalClass;
    private final Object delegate;
    private final Set<Method> methods;

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public T createProxy() {
        Constructor<?>[] constructors = new ByteBuddy()
                .subclass(originalClass)
                .method(buildMethodMatcher())
                .intercept(MethodDelegation.to(delegate))
                .make()
                .load(originalClass.getClassLoader())
                .getLoaded()
                .getDeclaredConstructors();
        Constructor<?> constructor = chooseConstructor(constructors);
        Object[] constructorFields = getConstructorFields(bean, originalClass, constructor.getParameterTypes());

        return (T) constructor.newInstance(constructorFields);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private ElementMatcher<? super MethodDescription> buildMethodMatcher() {
        Junction result = not(isPrivate())
                .and(not(isFinal()))
                .and(not(isStatic()));
        if (methods != null) {
            result = result.and(buildMatcherByMethods());
        }

        return result;
    }

    private ElementMatcher<? extends MethodDescription> buildMatcherByMethods() {
        return description -> {
            for (Method method : methods) {
                if (description.represents(method)) {
                    return true;
                }
            }
            return false;
        };
    }
}
