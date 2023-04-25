package ru.home.application

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification


/*
 * @created 25.04.2023
 * @author alexander
 */
@SpringBootTest
class ApplicationIT extends Specification {

    def "verify context starts"() {
        expect:
        true
    }
}
