package grails.plugins.i18nEnums.annotations

import grails.plugins.i18nEnums.DefaultNameCase
import grails.util.Holders
import org.grails.config.PropertySourcesConfig

class GrailsConfigDefaultAnnotatedEnumSpec extends AnnotationSpecification {

    def source = """
				package test
				import grails.plugins.i18nEnums.annotations.I18nEnum

				@I18nEnum
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
        clazz.ONE.defaultMessage == 'one'
        clazz.Two.defaultMessage == 'two'
        clazz.three.defaultMessage == 'three'
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
        clazz.ONE.codes == ['pre.DefaultAnnotatedEnum.ONE.post', 'pre.DefaultAnnotatedEnum.ONE.post', 'pre.DefaultAnnotatedEnum.one.post']
        clazz.Two.codes == ['pre.DefaultAnnotatedEnum.TWO.post', 'pre.DefaultAnnotatedEnum.Two.post', 'pre.DefaultAnnotatedEnum.two.post']
        clazz.three.codes == ['pre.DefaultAnnotatedEnum.THREE.post', 'pre.DefaultAnnotatedEnum.three.post', 'pre.DefaultAnnotatedEnum.three.post']
    }


}
