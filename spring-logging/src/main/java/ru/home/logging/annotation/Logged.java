package ru.home.logging.annotation;

import ru.home.logging.model.ProxyMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/*
 * @created 13.04.2023
 * @author alexander
 */
@Target({ TYPE, METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
    ProxyMode proxyMode() default ProxyMode.DEFAULT;
}
