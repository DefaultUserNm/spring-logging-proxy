package ru.home.logging.util.mode;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import ru.home.logging.model.LoggedClassMetadata;

import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.stream;

/*
 * @created 22.04.2023
 * @author alexander
 */
@Slf4j
@UtilityClass
public class ProxyModeResolver {
    public static ProxyMode resolve(LoggedClassMetadata data) {
        ProxyMode result = null;
        ProxyMode suggested = data.getMode();
        if (suggested.canUse(data)) {
            result = suggested;
        } else {
            for (ProxyMode mode : getModes()) {
                if (mode.canUse(data)) {
                    result = mode;
                    break;
                }
            }
        }
        Assert.notNull(result, "Failed to determine proxy mode for "
                + data.getOriginalClass().getCanonicalName());
        log.debug("Using {} proxy mode for {}", result, data.getOriginalClass().getCanonicalName());

        return result;
    }

    private List<ProxyMode> getModes() {
        return stream(ProxyMode.values())
                .sorted(Comparator.comparing(ProxyMode::getPriority))
                .toList();
    }
}