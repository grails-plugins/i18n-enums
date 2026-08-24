package app1

import grails.testing.mixin.integration.Integration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification

/**
 * Exercises the plugin the way a real application does: annotated and trait-based
 * enums resolved through Grails' own messageSource and rendered by a GSP.
 */
@Integration
class I18nEnumsIntegrationSpec extends Specification {

    @Shared
    RestTemplate restTemplate = new RestTemplate()

    private ResponseEntity<String> doGet(String path) {
        restTemplate.exchange("http://localhost:${serverPort}${path}", HttpMethod.GET, null, String)
    }

    void 'annotated enums pick up prefix and postfix from the application config'() {
        when:
        def response = doGet('/enums/labels?lang=en')

        then:
        response.statusCode == HttpStatus.OK

        and: 'codes are built from the configured prefix/postfix and the full class name'
        response.body.contains('enum.app1.OrderStatus.NEW.label = New order')
        response.body.contains('enum.app1.OrderStatus.PENDING.label = Awaiting shipment')

        and: 'an unmapped constant falls back to the CAPITALIZE default message'
        response.body.contains('enum.app1.OrderStatus.SHIPPED.label = Shipped')
    }

    void 'annotation members override the application config'() {
        when:
        def response = doGet('/enums/labels?lang=en')

        then: 'shortName = true drops the package from the code'
        response.body.contains('enum.Priority.LOW.label = Low priority')
        response.body.contains('enum.Priority.VERY_HIGH.label = Highest priority')

        and: 'defaultNameCase = ALL_CAPS splits the unmapped constant on underscores'
        response.body.contains('enum.Priority.HIGH.label = High')
    }

    void 'enums using I18nEnumTrait resolve the same way as annotated ones'() {
        when:
        def response = doGet('/enums/labels?lang=en')

        then:
        response.body.contains('enum.app1.ShippingMethod.STANDARD.label = Standard shipping')
        response.body.contains('enum.app1.ShippingMethod.EXPRESS.label = Express')
    }

    void 'resolved messages honour the requested locale'() {
        when:
        def response = doGet('/enums/labels?lang=da')

        then:
        response.body.contains('enum.app1.OrderStatus.NEW.label = Ny ordre')
        response.body.contains('enum.Priority.LOW.label = Lav prioritet')
    }

    void 'the index page renders every enum constant through the messageSource'() {
        when:
        def response = doGet('/')

        then:
        response.statusCode == HttpStatus.OK
        response.body.contains('OrderStatus.NEW')
        response.body.contains('New order')
        response.body.contains('enum.app1.OrderStatus.NEW.label')
    }
}
