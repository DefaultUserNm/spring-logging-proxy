package ru.home.logging.annotation;

import ru.home.logging.model.ProxyMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * @created 13.04.2023
 * @author alexander
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
    ProxyMode proxyMode() default ProxyMode.DEFAULT;
}
