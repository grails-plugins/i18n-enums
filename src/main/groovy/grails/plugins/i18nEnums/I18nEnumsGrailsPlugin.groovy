package grails.plugins.i18nEnums

import grails.plugins.Plugin

class I18nEnumsGrailsPlugin extends Plugin {
    def grailsVersion = "7.0.0 > *"

    def title = "I18nEnums Grails Plugin"
    def description = 'Adds an enumeration usable on Enums to easy add and implement the MessageSourceResolvable interface'
    def license = "APACHE"
    def documentation = "https://github.com/grails-plugins/i18n-enums"

    def developers = [
            [name: "Søren Berg Glasius", email: "soeren@glasius.dk"],
            [name: "Burt Beckwith", email: "burt@burtbeckwith.com"],
            [name: "James Daugherty", email: "jdaugherty@jdresources.net"],
            [name: "Brian Koehmstedt", email: "bk@koeh.net"],
    ]

    def issueManagement = [system: 'github', url: 'https://github.com/grails-plugins/i18n-enums/issues']
    def scm = [url: 'https://github.com/grails-plugins/i18n-enums/']
}
