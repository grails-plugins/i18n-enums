[![Maven Central](https://img.shields.io/maven-central/v/org.grails.plugins/i18n-enums)](https://central.sonatype.com/artifact/org.grails.plugins/i18n-enums)
[![License](https://img.shields.io/github/license/grails-plugins/i18n-enums)](https://www.apache.org/licenses/LICENSE-2.0)
[![CI](https://github.com/grails-plugins/i18n-enums/actions/workflows/ci.yml/badge.svg?event=push)](https://github.com/grails-plugins/i18n-enums/actions/workflows/ci.yml)

I18n Enums Grails Plugin
========================

Makes Grails enums translatable. Annotate an enum with `@I18nEnum` — or implement the
`I18nEnumTrait` — and it implements Spring's `MessageSourceResolvable`, so the enum constant can be
handed straight to `messageSource`, to `<g:message>`, or to anything else that resolves message
codes. The codes are derived from the enum's package, class and constant name, and can be tuned per
annotation or globally from the application configuration.

The user guide can be found here: 📚 [Documentation]

## Installation

Add the following dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'org.grails.plugins:i18n-enums:8.0.0-SNAPSHOT'
}
```

Requires Grails 8 (Java 21 or later, Groovy 5). For Grails 7.x use the `7.0.x` line.

## Usage

```groovy
import grails.plugins.i18nEnums.annotations.I18nEnum

@I18nEnum
enum OrderStatus {
    NEW, PENDING, SHIPPED
}
```

```groovy
messageSource.getMessage(OrderStatus.NEW, locale)   // resolves app.OrderStatus.NEW
```

See the [Documentation] for the trait flavour, prefix/postfix/shortName/defaultNameCase options, and
application-wide configuration. A runnable example lives in [`examples/app1`](examples/app1).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md). This repository is built on
[grails-plugin-template](https://github.com/grails-plugins/grails-plugin-template) — `build-logic/`,
`gradle/`, `.github/workflows/`, `.github/scripts/`, `.agents/`, `CONTRIBUTING.md` and `LICENSE.txt`
are kept up to date by its automated file sync and should not be edited here.

[Documentation]: https://grails-plugins.github.io/i18n-enums/
