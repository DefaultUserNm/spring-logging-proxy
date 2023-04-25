package ru.home.logging.logbook;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration;

/*
 * @created 21.04.2023
 * @author alexander
 */
@Configuration
@ConditionalOnProperty(
        value = "logging.logbook.autoconfigure",
        havingValue = "true",
        matchIfMissing = true
)
@AutoConfigureBefore(LogbookAutoConfiguration.class)
public class LogbookConfiguration {

    @Bean
    public HttpLogWriter httpLogWriter() {
        return new InfoHttpLogWriter();
    }
}
