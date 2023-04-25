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

import static net.bytebuddy.matcher.ElementMatchers.anyOf;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.isPrivate;
import static net.bytebuddy.matcher.ElementMatchers.not;
import static ru.home.logging.util.ObjectClassUtil.chooseConstructor;
import static ru.home.logging.util.ObjectClassUtil.getConstructorFields;

/*
 * @created 21.04.2023
 * @author alexander
 */
@RequiredArgsConstructor
public class ByteBuddyProxyFactory implements ProxyFactory {

    private final Object bean;
    private final Class<?> originalClass;
    private final Set<Method> methods;

    @Override
    @SneakyThrows
    public Object createProxy() {
        Constructor<?>[] constructors = new ByteBuddy()
                .subclass(originalClass)
                .method(buildMethodMatcher())
                .intercept(MethodDelegation.to(new LoggingMethodDelegator(bean)))
                .make()
                .load(originalClass.getClassLoader())
                .getLoaded()
                .getDeclaredConstructors();
        Constructor<?> constructor = chooseConstructor(constructors);
        Object[] constructorFields = getConstructorFields(bean, originalClass, constructor.getParameterTypes());

        return constructor.newInstance(constructorFields);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private ElementMatcher<? super MethodDescription> buildMethodMatcher() {
        Junction byMethodsMatcher = methods == null
                ? isDeclaredBy(originalClass)
                : anyOf(methods);

        return not(isPrivate()).and(byMethodsMatcher);
    }
}
