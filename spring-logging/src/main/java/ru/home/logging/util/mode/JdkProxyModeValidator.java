package ru.home.logging.util.mode;

import lombok.extern.slf4j.Slf4j;
import ru.home.logging.model.LoggedClassMetadata;

/*
 * @created 28.04.2023
 * @author alexander
 */
@Slf4j
public class JdkProxyModeValidator implements ProxyModeValidator {

    @Override
    public boolean canUse(LoggedClassMetadata data) {
        for (Class<?> ifc : data.getOriginalClass().getInterfaces()) {
            if (ifc.getMethods().length > 0) {
                log.debug(
                        "Class {} implements methods {} from {} interface",
                        data.getOriginalClass().getCanonicalName(), ifc.getMethods(), ifc
                );
                return true;
            }
        }
        log.debug("Class implements 0 interfaces with at least 1 method");
        return false;
    }
}