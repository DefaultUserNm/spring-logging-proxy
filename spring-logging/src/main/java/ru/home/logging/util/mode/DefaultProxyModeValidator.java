package ru.home.logging.util.mode;

import ru.home.logging.model.LoggedClassData;

/*
 * @created 28.04.2023
 * @author alexander
 */
public class DefaultProxyModeValidator implements ProxyModeValidator {

    @Override
    public boolean canUse(LoggedClassData data) {
        return false;
    }
}