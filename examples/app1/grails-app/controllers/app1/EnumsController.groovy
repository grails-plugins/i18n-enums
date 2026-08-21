package app1

import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.i18n.LocaleContextHolder

class EnumsController {

    MessageSource messageSource

    def index() {
        [resolvables: allResolvables, locale: LocaleContextHolder.locale]
    }

    /**
     * A machine-readable view of the same data, one '<first code> = <resolved message>'
     * line per enum constant. Used by the integration tests.
     */
    def labels() {
        Locale locale = params.lang ? Locale.forLanguageTag(params.lang as String) : LocaleContextHolder.locale
        render(contentType: 'text/plain', text: allResolvables.collect {
            "${it.codes.first()} = ${messageSource.getMessage(it, locale)}"
        }.join('\n'))
    }

    private List<MessageSourceResolvable> getAllResolvables() {
        [OrderStatus, Priority, ShippingMethod].collectMany {
            it.values().toList()
        } as List<MessageSourceResolvable>
    }
}
