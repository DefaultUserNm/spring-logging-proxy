package ru.home.logging.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Arrays;

/*
 * @created 14.04.2023
 * @author alexander
 */
@UtilityClass
class MethodUtil {
    static String calculateKey(Method method) {
        return method.getName() + "#" + Arrays.toString(method.getParameterTypes());
    }
}
