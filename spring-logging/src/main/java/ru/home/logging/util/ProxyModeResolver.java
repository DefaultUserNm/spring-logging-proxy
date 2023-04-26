package ru.home.logging.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.home.logging.annotation.Logged;
import ru.home.logging.model.LoggedClassData;
import ru.home.logging.model.ProxyMode;

import static ru.home.logging.model.ProxyMode.BYTE_BUDDY;
import static ru.home.logging.model.ProxyMode.CGLIB;
import static ru.home.logging.model.ProxyMode.DEFAULT;
import static ru.home.logging.model.ProxyMode.JDK;

/*
 * @created 22.04.2023
 * @author alexander
 */
@Slf4j
@UtilityClass
public class ProxyModeResolver {
    public static ProxyMode getProxyMode(Logged annotation) {
        ProxyMode proxyMode = annotation.proxyMode();
        return proxyMode == null
                ? DEFAULT
                : proxyMode;
    }

    public static ProxyMode resolve(LoggedClassData data) {
        ProxyMode result;
        ProxyMode suggested = data.getMode();
        if (suggested == null || suggested == DEFAULT) {
            result = getDefault();
        } else if (suggested == BYTE_BUDDY) {
            result = BYTE_BUDDY;
        } else if (suggested == JDK ) {
            if (canUseJdkProxy(data.getOriginalClass())) {
                result = JDK;
            } else {
                result = getDefault();
            }
        } else {
            result = CGLIB;
        }
        log.debug("Using {} proxy mode for {}", result, data.getOriginalClass().getCanonicalName());

        return result;
    }

    private ProxyMode getDefault() {
        return BYTE_BUDDY;
    }

    private boolean canUseJdkProxy(Class<?> beanClass) {
        for (Class<?> ifc : beanClass.getInterfaces()) {
            if (ifc.getMethods().length > 0) {
                log.debug(
                        "Class {} implements methods {} from {} interface",
                        beanClass.getCanonicalName(), ifc.getMethods(), ifc
                );
                return true;
            }
        }
        log.debug("Class implements 0 interfaces with at least 1 method");
        return false;
    }
}