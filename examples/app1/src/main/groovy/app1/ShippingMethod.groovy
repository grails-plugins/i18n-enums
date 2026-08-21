package app1

import grails.plugins.i18nEnums.traits.I18nEnumTrait

/**
 * The trait flavour - equivalent to a bare {@code @I18nEnum}, but without the AST
 * transformation. Configuration comes from {@code grails.plugin.i18nEnum} only.
 */
enum ShippingMethod implements I18nEnumTrait {
    STANDARD,
    EXPRESS
}
