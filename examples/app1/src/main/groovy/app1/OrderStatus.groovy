package app1

import grails.plugins.i18nEnums.annotations.I18nEnum

/**
 * A plain {@code @I18nEnum} - it inherits every setting from
 * {@code grails.plugin.i18nEnum} in application.yml, so the generated codes are
 * {@code enum.app1.OrderStatus.<NAME>.label} and the default message is capitalized.
 */
@I18nEnum
enum OrderStatus {
    NEW,
    PENDING,
    SHIPPED
}
