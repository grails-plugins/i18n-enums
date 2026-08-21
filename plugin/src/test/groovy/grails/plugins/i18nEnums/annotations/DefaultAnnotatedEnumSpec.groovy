package grails.plugins.i18nEnums.annotations
import org.springframework.context.MessageSourceResolvable

class DefaultAnnotatedEnumSpec extends AnnotationSpecification {

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


	def "test that the enum has an interface of MessageSourceResolvable"() {
		when:
		def clazz = add_class_to_classpath(source)

		then:
		clazz.ONE instanceof MessageSourceResolvable
	}


	def "test that the default annotated enum default message returns correct values"() {
		when:
		def clazz = add_class_to_classpath(source)

		then:
		clazz.ONE.defaultMessage == 'ONE'
		clazz.Two.defaultMessage == 'Two'
		clazz.three.defaultMessage == 'three'
	}



	def "test that the default annotated enum arguments returns correct values"() {
		when:
		def clazz = add_class_to_classpath(source)

		then:
		clazz.ONE.arguments == []
		clazz.Two.arguments == []
		clazz.three.arguments == []
	}

	def "test that the default annotated enum codes returns correct values"() {
		when:
		def clazz = add_class_to_classpath(source)

		then:
		clazz.ONE.codes == ['test.DefaultAnnotatedEnum.ONE','test.DefaultAnnotatedEnum.ONE', 'test.DefaultAnnotatedEnum.one']
		clazz.Two.codes == ['test.DefaultAnnotatedEnum.TWO','test.DefaultAnnotatedEnum.Two', 'test.DefaultAnnotatedEnum.two']
		clazz.three.codes == ['test.DefaultAnnotatedEnum.THREE','test.DefaultAnnotatedEnum.three', 'test.DefaultAnnotatedEnum.three']
	}


}
