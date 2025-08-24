package grails.plugins.i18nEnums.annotations

import grails.plugins.i18nEnums.DefaultNameCase
import grails.util.Holders
import org.grails.config.PropertySourcesConfig

class GrailsConfigOverrideAnnotatedEnumSpec extends AnnotationSpecification {

    def source = """
				package test
				import grails.plugins.i18nEnums.annotations.I18nEnum
                import grails.plugins.i18nEnums.DefaultNameCase

				@I18nEnum(prefix = 'overridepre', postfix = 'overridepost', shortName = false, defaultNameCase = DefaultNameCase.UPPER_CASE)
				enum DefaultAnnotatedEnum {
					ONE,
					Two,
					three
				}
			"""

    def setup() {
        Holders.config = new PropertySourcesConfig([ grails: [plugin: [i18nEnum: [
                prefix:'pre',
                postfix: 'post',
                defaultNameCase: DefaultNameCase.LOWER_CASE,
                shortName: true
        ]]]])
    }

    def cleanup() {
        Holders.config = null
    }

    def "test that the annotated enum default message returns correct values"() {
        when:
        def clazz = add_class_to_classpath(source)

        then:
        clazz.ONE.defaultMessage == 'ONE'
        clazz.Two.defaultMessage == 'TWO'
        clazz.three.defaultMessage == 'THREE'
    }

    def "test that the annotated enum arguments returns correct values"() {
        when:
        def clazz = add_class_to_classpath(source)

        then:
        clazz.ONE.arguments == []
        clazz.Two.arguments == []
        clazz.three.arguments == []
    }

    def "test that the annotated enum codes returns correct values"() {
        when:
        def clazz = add_class_to_classpath(source)

        then:
        clazz.ONE.codes == ['overridepre.test.DefaultAnnotatedEnum.ONE.overridepost', 'overridepre.test.DefaultAnnotatedEnum.ONE.overridepost', 'overridepre.test.DefaultAnnotatedEnum.one.overridepost']
        clazz.Two.codes == ['overridepre.test.DefaultAnnotatedEnum.TWO.overridepost', 'overridepre.test.DefaultAnnotatedEnum.Two.overridepost', 'overridepre.test.DefaultAnnotatedEnum.two.overridepost']
        clazz.three.codes == ['overridepre.test.DefaultAnnotatedEnum.THREE.overridepost', 'overridepre.test.DefaultAnnotatedEnum.three.overridepost', 'overridepre.test.DefaultAnnotatedEnum.three.overridepost']
    }
}
