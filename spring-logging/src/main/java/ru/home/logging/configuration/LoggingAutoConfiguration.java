package ru.home.logging.configuration;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @created 24.04.2023
 * @author alexander
 */
@Configuration
@ConditionalOnProperty(
        value = "logging.logger-bpp.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class LoggingAutoConfiguration {

    @Bean
    public BeanPostProcessor loggingConfigurerBeanPostProcessor() {
        return new LoggingConfigurerBeanPostProcessor();
    }
}
