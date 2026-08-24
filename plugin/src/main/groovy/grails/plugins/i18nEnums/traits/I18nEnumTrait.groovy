package grails.plugins.i18nEnums.traits

import grails.plugins.i18nEnums.DefaultNameCase
import grails.plugins.i18nEnums.transformation.I18nEnumTransformation
import grails.util.Holders
import groovy.transform.SelfType
import org.springframework.context.MessageSourceResolvable

import java.lang.reflect.Field

@SelfType(Enum)
trait I18nEnumTrait implements MessageSourceResolvable {

    /**
     * The name() method on enums
     * @return
     */
    abstract String name()

    @Override
    String[] getCodes() {
        Boolean shortName = getConfigProperty('shortName', Boolean, false)
        String className = shortName ? this.class.simpleName : this.class.name

        [name.toUpperCase(), name, name.toLowerCase()].collect {
            "${getI18nEnumPrefix()}${className}.${it}${getI18nEnumPostfix()}".toString()
        } as String[]
    }

    @Override
    Object[] getArguments() {
        [] as Object[]
    }

    @Override
    String getDefaultMessage() {
        DefaultNameCase defaultNameCase = getConfigProperty('defaultNameCase', DefaultNameCase, null)
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
     * The config baked into the enum by I18nEnumTransformation when @I18nEnum carries members,
     * or an empty map for a bare @I18nEnum or a plain I18nEnumTrait implementation.
     *
     * Read reflectively from the static field the transformation adds, rather than through an
     * overridable trait method: a method declared here would shadow the enum's own on every
     * call made from inside the trait, which is exactly where it is needed.
     */
    private Map getI18nEnumASTConfig() {
        Field field = this.class.declaredFields.find { it.name == I18nEnumTransformation.AST_CONFIG_FIELD }
        field ? field.get(null) as Map : [:]
    }

    /**
     * Given a property name, look that property up under the i18nEnum config.
     * If the I18nEnumTrait is used from an annotation, the annotation config overrules
     * the Grails config settings.
     * @return a config (empty if no config)
     */
    private <T> T getConfigProperty(String propertyName, Class<T> type, T defaultValue) {
        Map astConfig = getI18nEnumASTConfig()
        if (astConfig.containsKey(propertyName)) {
            T foundValue = astConfig[propertyName] as T
            return foundValue == null ? defaultValue : foundValue
        }

        Holders.config?.getProperty("grails.plugin.i18nEnum.${propertyName}" as String, type, defaultValue)
    }

    /**
     * Normalizes the prefix
     * @return
     */
    private String getI18nEnumPrefix() {
        String prefix = getConfigProperty('prefix', String, null)
        prefix ? prefix + (prefix.endsWith('.') ? '' : '.') : ''
    }

    /**
     * Normalizes the postfix
     * @return
     */
    private String getI18nEnumPostfix() {
        String postfix = getConfigProperty('postfix', String, null)
        postfix ? (postfix.startsWith('.') ? '' : '.') + postfix : ''
    }
}
