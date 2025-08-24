package grails.plugins.i18nEnums.annotations

import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import grails.plugins.i18nEnums.DefaultNameCase

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@CompileStatic
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@GroovyASTTransformationClass("grails.plugins.i18nEnums.transformation.I18nEnumTransformation")
public @interface I18nEnum {
    String prefix() default ''

    String postfix() default ''

    boolean shortName() default false

    DefaultNameCase defaultNameCase() default DefaultNameCase.UNCHANGED
}
