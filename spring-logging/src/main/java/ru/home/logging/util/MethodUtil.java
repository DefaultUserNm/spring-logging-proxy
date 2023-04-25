package ru.home.logging.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;

/*
 * @created 14.04.2023
 * @author alexander
 */
@UtilityClass
public class MethodUtil {
    public static String calculateKey(Method method) {
        return method.getName() + "#" + method.getParameterCount();
    }
}
