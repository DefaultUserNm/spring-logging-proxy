package ru.home.logging.util.mode;

import ru.home.logging.model.LoggedClassMetadata;

import static java.lang.reflect.Modifier.isFinal;

/*
 * @created 28.04.2023
 * @author alexander
 */
public class ByteBuddyProxyModeValidator implements ProxyModeValidator {

    @Override
    public boolean canUse(LoggedClassMetadata data) {
        return !isFinal(data.getOriginalClass().getModifiers());
    }
}