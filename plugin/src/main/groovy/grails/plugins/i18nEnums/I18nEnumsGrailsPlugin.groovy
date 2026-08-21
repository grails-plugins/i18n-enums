package grails.plugins.i18nEnums

import grails.plugins.Plugin

class I18nEnumsGrailsPlugin extends Plugin {

    def grailsVersion = '8.0.0 > *'

    def title = 'I18n Enums Plugin'
    def description = 'Adds an annotation usable on Enums to easily add and implement the MessageSourceResolvable interface'
    def license = 'APACHE'
    def documentation = 'https://grails-plugins.github.io/i18n-enums/'
    def organization = [name: 'Grails Plugins', url: 'https://github.com/grails-plugins']

    def developers = [
            [name: 'Søren Berg Glasius', email: 'soeren@glasius.dk'],
            [name: 'Burt Beckwith', email: 'burt@burtbeckwith.com'],
            [name: 'James Daugherty', email: 'jdaugherty@jdresources.net'],
            [name: 'Brian Koehmstedt', email: 'bk@koeh.net'],
    ]

    def issueManagement = [system: 'github', url: 'https://github.com/grails-plugins/i18n-enums/issues']
    def scm = [url: 'https://github.com/grails-plugins/i18n-enums/']
}
