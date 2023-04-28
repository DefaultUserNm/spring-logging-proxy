package ru.home.logging.util.mode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.home.logging.model.LoggedClassData;

import static java.lang.Integer.MAX_VALUE;

/*
 * @created 15.04.2023
 * @author alexander
 */
@Getter
@RequiredArgsConstructor
public enum ProxyMode {
    DEFAULT(MAX_VALUE, new DefaultProxyModeValidator()),
    JDK(1, new JdkProxyModeValidator()),
    CGLIB(2, new CglibProxyModeValidator()),
    BYTE_BUDDY(0, new ByteBuddyProxyModeValidator());

    private final Integer priority;
    private final ProxyModeValidator validator;

    public boolean canUse(LoggedClassData data) {
        return this.validator.canUse(data);
    }
}
