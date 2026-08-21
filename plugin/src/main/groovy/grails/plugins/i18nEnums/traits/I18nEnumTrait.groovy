package grails.plugins.i18nEnums.traits

import grails.plugins.i18nEnums.DefaultNameCase
import grails.util.Holders
import groovy.transform.SelfType
import org.springframework.context.MessageSourceResolvable

@SelfType(Enum)
trait I18nEnumTrait implements MessageSourceResolvable {
    /**
     * The name() method on enums
     * @return
     */
    abstract String name()

    @Override
    String[] getCodes() {
        Boolean shortName = this.getConfigProperty('shortName', Boolean, false)
        String className = shortName ? this.class.simpleName : this.class.name

        [name.toUpperCase(), name, name.toLowerCase()].collect {
            "${i18nEnumPrefix}${className}.${it}${i18nEnumPostfix}".toString()
        } as String[]
    }

    @Override
    Object[] getArguments() {
        [] as Object[]
    }

    @Override
    String getDefaultMessage() {
        DefaultNameCase defaultNameCase = this.getConfigProperty('defaultNameCase', DefaultNameCase, null)
        switch (defaultNameCase) {
            case DefaultNameCase.UPPER_CASE:
                return name.toUpperCase()
            case DefaultNameCase.LOWER_CASE:
                return name.toLowerCase()
            case DefaultNameCase.CAPITALIZE:
                return name.toLowerCase().capitalize()
            case DefaultNameCase.ALL_CAPS:
                return name.split('_').collect { String it -> it.toLowerCase().capitalize() }.join(' ')
            default:
                name
        }
    }

    /**
     * Convenient method to get name() return value.
     * @return
     */
    String getName() {
        name()
    }

    /**
     * This method returns an empty map. The method body can be replaced
     * by the I18nEnumTransformation if the @I18nEnum provides arguments
     * @return and empty config unless replaced by the AST transformation.
     */
    private static Map getI18nEnumASTConfig() {
        [:]
    }

    /**
     * Given a property name, lookup the that property under the i18nEnumConfig
     * If the I18nEnumTrait is used from an annotation, the annotation config overrules
     * the Grails config settings.
     * @return a config map (Empty if no config)
     */
    private static <T> T getConfigProperty(String propertyName, Class<T> type, T defaultValue) {
        if(i18nEnumASTConfig.containsKey(propertyName)) {
            T foundValue = i18nEnumASTConfig[propertyName] as T
            return foundValue == null ? defaultValue : foundValue
        }

        Holders.config?.getProperty("grails.plugin.i18nEnum.${propertyName}" as String, type, defaultValue)
    }

    /**
     * Normalizes the prefix
     * @return
     */
    private static String getI18nEnumPrefix() {
        String prefix = this.getConfigProperty('prefix', String, null)
        return prefix ? prefix + (prefix.endsWith('.') ? '' : '.') : ''
    }

    /**
     * Normalizes the postfix
     * @return
     */
    private static String getI18nEnumPostfix() {
        String postfix = this.getConfigProperty('postfix', String, null)
        return postfix ? (postfix.startsWith('.') ? '' : '.') + postfix : ''
    }
}
