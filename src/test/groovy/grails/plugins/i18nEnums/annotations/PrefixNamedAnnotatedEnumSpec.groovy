package grails.plugins.i18nEnums.annotations

import spock.lang.Unroll

class PrefixNamedAnnotatedEnumSpec extends AnnotationSpecification {

	def source = '''
				package test
				import grails.plugins.i18nEnums.annotations.I18nEnum
				import grails.plugins.i18nEnums.DefaultNameCase

				@I18nEnum(${args})
				enum PrefixNameCasedAnnotatedEnum {
					ONE,
					two,
					Three,
					FOUR_Five
				}
			'''

	@Unroll
	def "test that the default annotated enum default message returns correct values"() {
		when:
		def args = []
		if(prefix) args << "prefix = '${prefix}'"
		if(postfix) args << "postfix = '${postfix}'"
        def src = createSourceCodeForTemplate(source, [args: args.join(", ")])
        def clazz = add_class_to_classpath(src)


		then:
		clazz.ONE.codes == createCodeList('ONE', expectedPrefix, expectedPostfix)
		clazz.two.codes == createCodeList('two', expectedPrefix, expectedPostfix)
		clazz.Three.codes == createCodeList('Three', expectedPrefix, expectedPostfix)
		clazz.FOUR_Five.codes == createCodeList('FOUR_Five', expectedPrefix, expectedPostfix)

		where:
		prefix | postfix | expectedPrefix | expectedPostfix
		''     | ''      | ''             | ''
		'pre'  | ''      | 'pre.'         | ''
		'pre.' | ''      | 'pre.'         | ''
		''     | 'post'  | ''             | '.post'
		''     | '.post' | ''             | '.post'
		'pre'  | 'post'  | 'pre.'         | '.post'
		'pre.' | '.post' | 'pre.'         | '.post'
	}


	private createCodeList(name, prefix, postfix) {
		[
				createEnumName(name.toUpperCase(), prefix, postfix),
				createEnumName(name, prefix, postfix),
				createEnumName(name.toLowerCase(), prefix, postfix)
		]
	}

	private String createEnumName(name, prefix, postfix) {
		"${prefix}test.PrefixNameCasedAnnotatedEnum.${name}${postfix}".toString()
	}
}
