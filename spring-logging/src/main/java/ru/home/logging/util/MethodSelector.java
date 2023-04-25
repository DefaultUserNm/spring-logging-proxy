package ru.home.logging.util;

import java.lang.reflect.Method;

/*
 * @created 25.04.2023
 * @author alexander
 */
@FunctionalInterface
public interface MethodSelector {
    boolean matches(Method method);
}
