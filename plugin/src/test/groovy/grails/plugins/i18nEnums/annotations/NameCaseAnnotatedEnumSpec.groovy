package grails.plugins.i18nEnums.annotations

import spock.lang.Unroll

class NameCaseAnnotatedEnumSpec extends AnnotationSpecification {

	def source = '''
				package test
				import grails.plugins.i18nEnums.annotations.I18nEnum
				import grails.plugins.i18nEnums.DefaultNameCase

				@I18nEnum(defaultNameCase = DefaultNameCase.${nameCase})
				enum NameCasedAnnotatedEnum {
					ONE,
					two,
					Three,
					FOUR_FIVE
				}
			'''

	@Unroll
	def "test that the default annotated enum default message returns correct values"() {

		when:
		def clazz = add_class_to_classpath(createSourceCodeForTemplate(source, [nameCase: enumName]))

		then:
		clazz.ONE.defaultMessage == one
		clazz.two.defaultMessage == two
		clazz.Three.defaultMessage == three
		clazz.FOUR_FIVE.defaultMessage == four

		where:
		enumName     | one   | two   | three   | four
		'UNCHANGED'  | 'ONE' | 'two' | 'Three' | 'FOUR_FIVE'
		'UPPER_CASE' | 'ONE' | 'TWO' | 'THREE' | 'FOUR_FIVE'
		'LOWER_CASE' | 'one' | 'two' | 'three' | 'four_five'
		'CAPITALIZE' | 'One' | 'Two' | 'Three' | 'Four_five'
		'ALL_CAPS' | 'One' | 'Two' | 'Three' | 'Four Five'
	}
}
