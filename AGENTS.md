# AGENTS.md - i18n-enums

## Project Overview

The **I18n Enums** Grails plugin makes enums translatable. An enum annotated with `@I18nEnum` (or
implementing `I18nEnumTrait`) implements Spring's `MessageSourceResolvable`, so its constants can be
resolved directly by Grails' `messageSource` or `<g:message>`.

- **Language:** Groovy 4.0.30 on Java 17
- **Framework:** Grails 7.x
- **Build System:** Gradle 8.14.4 (with wrapper)
- **Published artifact:** `org.grails.plugins:i18n-enums`
- **Current Version:** 7.0.2-SNAPSHOT
- **License:** Apache 2.0

This repository is built on
[grails-plugin-template](https://github.com/grails-plugins/grails-plugin-template). `build-logic/`,
`gradle/`, `.github/workflows/`, `.github/scripts/`, `.agents/`, `CONTRIBUTING.md` and `LICENSE.txt`
are delivered by its automated file sync — **do not edit them here**; changes belong upstream in the
template. Everything else in this repository is project-owned.

## Skill Files (Best Practices)

Detailed best practices are documented as skills in `.agents/skills/` (`.claude` is a symlink to `.agents`):

| Skill                                                                   | Purpose                                               |
|-------------------------------------------------------------------------|-------------------------------------------------------|
| [`repository-structure`](.agents/skills/repository-structure/SKILL.md)  | Canonical directory layout and architectural rules    |
| [`gradle-best-practices`](.agents/skills/gradle-best-practices/SKILL.md)| Gradle best practices, convention plugins, and idioms |
| [`plugin-project`](.agents/skills/plugin-project/SKILL.md)              | Plugin project scope: source code + unit tests only   |
| [`example-apps`](.agents/skills/example-apps/SKILL.md)                  | Example app patterns: integration & functional tests  |

**Read these skill files before making structural changes to the repository.**

## Critical Rules

1. **NEVER add code to the root `build.gradle` to configure subprojects.** No `subprojects {}`, `allprojects {}`, or
   `configure()` blocks. All shared configuration goes through convention plugins in `build-logic/`.
2. **The plugin project contains ONLY plugin code and unit tests.** No integration tests, no functional tests, no
   example controllers or views.
3. **Example apps under `examples/` host all integration and functional tests.** They depend on the plugin via
   `implementation project(':i18n-enums')` and test it as a real consumer would.
4. **Use Gradle convention plugins to deduplicate.** If two or more subprojects share build logic, extract it into a
   convention plugin in `build-logic/` — which means proposing it upstream in the template.
5. **Always use lazy Gradle APIs** to avoid eager initialization (`tasks.register()`, `tasks.named()`, `configureEach`,
   `provider {}`).

## Repository Structure

```
i18n-enums/
├── .agents/skills/      # Agent skill files (.claude is a symlink to .agents)
├── plugin/              # Core Grails plugin (artifact: i18n-enums)
│   └── src/main/groovy/grails/plugins/i18nEnums/
│       ├── annotations/     #   @I18nEnum
│       ├── traits/          #   I18nEnumTrait
│       └── transformation/  #   I18nEnumTransformation (AST)
├── examples/app1/       # Example Grails app exercising the plugin
├── docs/                # Asciidoctor documentation
├── build-logic/         # Gradle convention plugins (composite build)
├── code-coverage/       # JaCoCo aggregation module
├── .github/workflows/   # CI, release, and release-notes workflows
├── build.gradle         # Root build file (docs + root-publish ONLY)
├── settings.gradle      # Multi-project settings
├── gradle.properties    # Version properties
└── project.yml          # Project metadata (POM, docs, release notes, version index)
```

## Build and Test Commands

```bash
# Full build (compile + test + code style)
./gradlew build

# Run only unit tests (plugin module)
./gradlew :i18n-enums:test

# Run integration tests (example app)
./gradlew :app1:integrationTest

# Skip tests
./gradlew build -PskipTests

# Run the example app (http://localhost:8080/)
./gradlew :app1:bootRun

# Generate documentation
./gradlew docs

# Clean build
./gradlew clean build

# Run code style checks only
./gradlew codeStyle

# Skip code style checks
./gradlew build -PskipCodeStyle
```

## SDK Requirements

Use SDKMAN to install the correct tool versions (see `.sdkmanrc`):

- Java: `17.0.18-librca`
- Gradle: `8.14.4`
- Groovy: `4.0.30`

Run `sdk env install` to set up the environment.

## Architecture

Two entry points, one implementation:

1. **`@I18nEnum`** — a `SOURCE`-retention annotation handled by `I18nEnumTransformation` at the
   `CANONICALIZATION` compile phase. The transformation adds `I18nEnumTrait` to the enum via
   `TraitComposer`, then, if the annotation carries any members, rewrites the body of the trait's
   `getI18nEnumASTConfig()` to return them as a map literal.
2. **`I18nEnumTrait`** — implemented directly on an enum when no annotation-level configuration is
   needed. It supplies `getCodes()`, `getArguments()` and `getDefaultMessage()`.

Configuration resolution order, per property: the AST config baked in by the annotation wins;
otherwise `grails.plugin.i18nEnum.<property>` is read from the Grails config via `Holders.config`.

### Core Classes

| Class / Interface         | Location                                                   | Purpose                                            |
|---------------------------|------------------------------------------------------------|----------------------------------------------------|
| `I18nEnum`                | `plugin/src/main/groovy/.../annotations/`                  | The annotation; carries prefix/postfix/shortName/defaultNameCase |
| `I18nEnumTransformation`  | `plugin/src/main/groovy/.../transformation/`                | AST transformation: adds the trait, bakes in config |
| `I18nEnumTrait`           | `plugin/src/main/groovy/.../traits/`                        | `MessageSourceResolvable` implementation            |
| `DefaultNameCase`         | `plugin/src/main/groovy/.../`                               | Casing strategies for the default message           |
| `I18nEnumsGrailsPlugin`   | `plugin/src/main/groovy/.../`                               | Plugin descriptor                                   |

## Configuration

Application-wide defaults, overridden per enum by `@I18nEnum` members:

```yaml
grails:
  plugin:
    i18nEnum:
      prefix: enum
      postfix: label
      shortName: false
      defaultNameCase: CAPITALIZE   # UPPER_CASE | LOWER_CASE | CAPITALIZE | ALL_CAPS | UNCHANGED
```

## Testing

### Unit Tests (`plugin/src/test/`)

Spock on the JUnit Platform. Because `@I18nEnum` is an AST transformation, the specs compile enum
sources at runtime: `AnnotationSpecification` is the shared base class that turns a source string
into a class via a `GroovyClassLoader`, so a spec can assert on the *result* of the transformation.
Specs needing Grails config set `Holders.config` to a `PropertySourcesConfig` in `setup()` and null
it out in `cleanup()`.

### Integration Tests (`examples/app1/`)

`examples/app1` is a real Grails app depending on the plugin. It declares three enums —
`OrderStatus` (plain `@I18nEnum`), `Priority` (annotation members overriding the app config) and
`ShippingMethod` (`I18nEnumTrait`) — plus message bundles in `grails-app/i18n/`. `EnumsController`
resolves them through Grails' `messageSource`; `I18nEnumsIntegrationSpec` asserts the resolved
messages, the generated codes, the `defaultMessage` fallback for unmapped constants, and locale
switching.

## CI/CD

- **CI** (`.github/workflows/ci.yml`): Builds and tests on push/PR; publishes snapshots to Maven Central Snapshots on
  push to release branches.
- **Release** (`.github/workflows/release.yml`): 4-stage pipeline triggered by GitHub release — stage artifacts, release
  to Maven Central, publish docs to GitHub Pages, bump version.
- **Release Notes** (`.github/workflows/release-notes.yml`): Auto-drafts release notes using release-drafter with
  category labels.

## Code Conventions

- Groovy source files use standard Grails conventions (services and taglibs in `grails-app/`, other classes in
  `src/main/groovy/`).
- **Use `def` for local variables** where the type is inferred from the right-hand side (e.g., constructor calls,
  method calls, casts, factory methods). Explicit types should only be used for local variables when the type cannot
  be inferred or when needed for `@CompileStatic` compilation. This applies to both production code and tests.
- Single-quoted strings unless interpolation is needed — CodeNarc's `UnnecessaryGString` is enforced at zero tolerance,
  as is `Indentation` (4 spaces, no tabs).
- When writing Gradle, always use the latest best practices to avoid eager initialization.
