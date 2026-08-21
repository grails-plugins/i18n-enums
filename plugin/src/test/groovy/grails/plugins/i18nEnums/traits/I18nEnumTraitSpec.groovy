package grails.plugins.i18nEnums.traits

import grails.plugins.i18nEnums.DefaultNameCase
import grails.util.Holders
import org.grails.config.PropertySourcesConfig
import org.springframework.context.MessageSourceResolvable
import spock.lang.Specification

class I18nEnumTraitSpec extends Specification {
    def cleanup() {
        Holders.config = null
    }

    def "test that the enum implementing I18nEnumTrait is instance of MessageSourceResolvable"() {
        expect:
        EnumImplementsTrait.ONE instanceof MessageSourceResolvable
    }

    def "test that the enum implementing I18nEnumTrait returns correct default message"() {
        expect:
        EnumImplementsTrait.ONE.defaultMessage == 'ONE'
        EnumImplementsTrait.Two.defaultMessage == 'Two'
        EnumImplementsTrait.three.defaultMessage == 'three'
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "test that the enum implementing I18nEnumTrait returns correct arguments"() {
        expect:
        EnumImplementsTrait.ONE.arguments == [] as Object[]
        EnumImplementsTrait.Two.arguments == [] as Object[]
        EnumImplementsTrait.three.arguments == [] as Object[]
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "test that the enum implementing I18nEnumTrait returns correct codes"() {
        expect:
        EnumImplementsTrait.ONE.codes == ['grails.plugins.i18nEnums.traits.EnumImplementsTrait.ONE', 'grails.plugins.i18nEnums.traits.EnumImplementsTrait.ONE', 'grails.plugins.i18nEnums.traits.EnumImplementsTrait.one'] as String[]
        EnumImplementsTrait.Two.codes == ['grails.plugins.i18nEnums.traits.EnumImplementsTrait.TWO', 'grails.plugins.i18nEnums.traits.EnumImplementsTrait.Two', 'grails.plugins.i18nEnums.traits.EnumImplementsTrait.two'] as String[]
        EnumImplementsTrait.three.codes == ['grails.plugins.i18nEnums.traits.EnumImplementsTrait.THREE', 'grails.plugins.i18nEnums.traits.EnumImplementsTrait.three', 'grails.plugins.i18nEnums.traits.EnumImplementsTrait.three'] as String[]
    }

    def "test that the enum implementing I18EnumTrait returns correct default message when setting config"() {
        given:
        setConfig(defaultNameCase: DefaultNameCase.UPPER_CASE)

        expect:
        EnumImplementsTrait.ONE.defaultMessage == 'ONE'
        EnumImplementsTrait.Two.defaultMessage == 'TWO'
        EnumImplementsTrait.three.defaultMessage == 'THREE'
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "test that the enum implementing I18nEnumTrait returns correct codes with config: shortName"() {
        given:
        setConfig(shortName: true)

        expect:
        EnumImplementsTrait.ONE.codes == ['EnumImplementsTrait.ONE', 'EnumImplementsTrait.ONE', 'EnumImplementsTrait.one'] as String[]
        EnumImplementsTrait.Two.codes == ['EnumImplementsTrait.TWO', 'EnumImplementsTrait.Two', 'EnumImplementsTrait.two'] as String[]
        EnumImplementsTrait.three.codes == ['EnumImplementsTrait.THREE', 'EnumImplementsTrait.three', 'EnumImplementsTrait.three'] as String[]
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "test that the enum implementing I18nEnumTrait returns correct codes with config: prefix and  postfix"() {
        given:
        setConfig(prefix: 'pre', postfix: '.post')

        expect:
        EnumImplementsTrait.ONE.codes ==   ['pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.ONE.post', 'pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.ONE.post', 'pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.one.post'] as String[]
        EnumImplementsTrait.Two.codes ==   ['pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.TWO.post', 'pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.Two.post', 'pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.two.post'] as String[]
        EnumImplementsTrait.three.codes == ['pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.THREE.post', 'pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.three.post', 'pre.grails.plugins.i18nEnums.traits.EnumImplementsTrait.three.post'] as String[]
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "test that the enum implementing I18nEnumTrait returns correct codes with config: prefix, postfix and shortName"() {
        given:
        setConfig(prefix: 'prefix.', postfix: 'postfix', shortName: true)

        expect:
        EnumImplementsTrait.ONE.codes == ['prefix.EnumImplementsTrait.ONE.postfix', 'prefix.EnumImplementsTrait.ONE.postfix', 'prefix.EnumImplementsTrait.one.postfix'] as String[]
        EnumImplementsTrait.Two.codes == ['prefix.EnumImplementsTrait.TWO.postfix', 'prefix.EnumImplementsTrait.Two.postfix', 'prefix.EnumImplementsTrait.two.postfix'] as String[]
        EnumImplementsTrait.three.codes == ['prefix.EnumImplementsTrait.THREE.postfix', 'prefix.EnumImplementsTrait.three.postfix', 'prefix.EnumImplementsTrait.three.postfix'] as String[]
    }

    private static setConfig(Map args) {
        Holders.config = new PropertySourcesConfig([grails: [plugin: [i18nEnum: args]]])
    }
}

enum EnumImplementsTrait implements I18nEnumTrait {
    ONE,
    Two,
    three
}
