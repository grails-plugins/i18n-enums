package grails.plugins.i18nEnums.annotations

class AlreadyImplementedMethodAnnotatedEnumSpec extends AnnotationSpecification {

    def source = {
        """
            package test

            import grails.plugins.i18nEnums.annotations.I18nEnum
            import org.springframework.context.MessageSourceResolvable


            @I18nEnum
            public enum Test implements MessageSourceResolvable {
                ONE,
                two,
                Three

                // Where the method goes
                ${it}

            }
			"""
    }


    def "test that if the getCodes is already implemented, it is not being implemented again"() {

        when:
        def clazz = add_class_to_classpath(source("""
                @Override
                String[] getCodes() {
                    return ['some.other.code'] as String[]
                }"""))

        then:
        clazz.ONE.codes == ['some.other.code']
        clazz.two.codes == ['some.other.code']
        clazz.Three.codes == ['some.other.code']
    }

    def "test that if the getArguments is already implemented, it is not being implemented again"() {

        when:
        def clazz = add_class_to_classpath(source("""
                @Override
                Object[] getArguments() {
                    return [1,'A'] as Object[]
                }"""))

        then:
        clazz.ONE.arguments == [1, 'A']
        clazz.two.arguments == [1, 'A']
        clazz.Three.arguments == [1, 'A']
    }

    def "test that if the getDefaultMessage is already implemented, it is not being implemented again"() {

        when:
        def clazz = add_class_to_classpath(source("""
                @Override
                String getDefaultMessage() {
                    return "Another default message"
                }"""))

        then:
        clazz.ONE.defaultMessage == "Another default message"
        clazz.two.defaultMessage == "Another default message"
        clazz.Three.defaultMessage == "Another default message"
    }
}
