import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import ru.home.logging.configuration.LoggingAutoConfiguration
import ru.home.logging.configuration.LoggingConfigurerBeanPostProcessor
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat;

/*
 * @created 26.04.2023
 * @author alexander
 */
class LoggingAutoconfigurationIT extends Specification {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LoggingAutoConfiguration))

    def "verify LoggingConfigurerBeanPostProcessor is registered in ApplicationContext by default"() {
        expect:
        this.contextRunner.run(context) -> {
                assertThat(context).hasSingleBean(LoggingConfigurerBeanPostProcessor)
        }
    }

    def "verify LoggingConfigurerBeanPostProcessor is not registered in ApplicationContext \
        when 'logging.logger-bpp.enabled' is set to 'false'"() {
        expect:
        this.contextRunner
                .withPropertyValues("logging.logger-bpp.enabled=false")
                .run(context) -> {
            assertThat(context).doesNotHaveBean(LoggingConfigurerBeanPostProcessor)
        }
    }
}