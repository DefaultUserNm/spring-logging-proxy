package ru.home.logging.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.home.logging.util.mode.ProxyMode;

import java.lang.reflect.Method;
import java.util.Set;

/*
 * @created 15.04.2023
 * @author alexander
 */
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggedClassMetadata {
    Class<?> originalClass;
    ProxyMode mode;
    Set<Method> methods;
}