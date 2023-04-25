import org.springframework.boot.test.context.SpringBootTest
import ru.home.application.Application
import spock.lang.Specification

/*
 * @created 25.04.2023
 * @author alexander
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [
                Application
        ]
)
class ApplicationIT extends Specification {

    def "verify context starts"() {
        expect:
        true
    }
}
