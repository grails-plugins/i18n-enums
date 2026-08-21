package app1

import grails.plugins.i18nEnums.DefaultNameCase
import grails.plugins.i18nEnums.annotations.I18nEnum

/**
 * Annotation members override the application config, so these codes drop the
 * package name ({@code enum.Priority.<NAME>.label}) and the default message
 * splits on underscores ({@code VERY_HIGH} -> {@code Very High}).
 */
@I18nEnum(shortName = true, defaultNameCase = DefaultNameCase.ALL_CAPS)
enum Priority {
    LOW,
    HIGH,
    VERY_HIGH
}
