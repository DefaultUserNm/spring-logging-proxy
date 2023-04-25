package ru.home.logging.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.function.UnaryOperator.identity;

/*
 * @created 14.04.2023
 * @author alexander
 */
@UtilityClass
class ObjectClassUtil {
    public static Constructor<?> getConstructor(Object bean) {
        Constructor<?> result = getNoArgsConstructor(bean);
        if (result != null) {
            return result;
        }
        return bean.getClass().getConstructors()[0];
    }

    public static Constructor<?> chooseConstructor(Constructor<?>[] constructors) {
        Constructor<?> result = getNoArgsConstructor(constructors);
        if (result != null) {
            return result;
        }
        return constructors[0];
    }

    @SneakyThrows({ IllegalAccessException.class })
    public static Object[] getConstructorFields(Object bean, Class<?> originalClass, Class<?>[] argumentTypes) {
        List<Object> result = new ArrayList<>();
        Map<Class<?>, Field> typesMap = stream(originalClass.getDeclaredFields())
                .collect(Collectors.toMap(Field::getType, identity()));
        for (Class<?> argumentClass : argumentTypes) {
            Field field = typesMap.get(argumentClass);
            if (field == null) {
                throw new NullPointerException("Failed to find field by type: " + argumentClass.getCanonicalName());
            }
            field.setAccessible(true);
            result.add(field.get(bean));
        }

        return result.toArray(new Object[]{});
    }

    private Constructor<?> getNoArgsConstructor(Object bean) {
        return getNoArgsConstructor(bean.getClass().getConstructors());
    }

    private Constructor<?> getNoArgsConstructor(Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameters().length == 0) {
                return constructor;
            }
        }
        return null;
    }
}
