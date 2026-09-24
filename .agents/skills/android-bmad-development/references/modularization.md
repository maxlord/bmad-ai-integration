# Multi-module structure with feature api/impl

Apply these rules when the project is multi-module. Read `settings.gradle.kts`, the `build-logic` or `buildSrc` directory, and `gradle/libs.versions.toml` before editing any build file. The project's real module graph wins over this guide.

## Reference module layout

```text
:app                      application, navigation host, Hilt entry point; depends on every :feature:*:impl
:feature:<name>:api       public contract of a feature: navigation route/entry, interfaces, models other features need
:feature:<name>:impl      screens, ViewModels, repositories, Hilt modules for that feature
:core:model               plain Kotlin models shared across features
:core:data                shared repositories and data sources
:core:domain              optional shared use cases
:core:ui / :core:designsystem   shared composables, theme, resources
:core:common              dispatchers, result wrappers, small utilities
:core:testing             fakes, test rules, test runner shared by tests
```

## Dependency rules

- `impl` modules depend on their own `api`, other features' `api`, and `core` modules. **An `impl` never depends on another `impl`.**
- `api` modules stay small and stable: route definitions, navigation extension functions, interfaces, and models. No screens, ViewModels, Hilt modules, or Android UI dependencies unless the project's pattern requires them.
- `core` modules never depend on `feature` modules.
- Use `implementation` by default. Use `api(...)` in Gradle only when a type from the dependency appears in this module's public signatures.
- A dependency cycle or an `impl`-to-`impl` edge means the contract belongs in an `api` module or in `core`. Move the contract; do not work around it.

## Navigation between features

- The `api` module exposes the route (for example a `@Serializable` route object for type-safe Navigation Compose) and a `NavController.navigateToX(...)` extension.
- The `impl` module exposes a `NavGraphBuilder.xScreen(...)` extension that registers its destination and receives navigation callbacks as lambdas.
- `:app` wires the graph. Features never navigate by referencing another feature's screen directly.

## Where a change goes

1. Change only the owning feature's `impl` when possible.
2. Change an `api` module only when other modules need the new contract. Treat `api` changes as public API: keep them minimal and mention them in the summary.
3. Move code into `core` only when a second feature needs it now, not in anticipation.
4. Create a new module only when the accepted specification introduces a new feature or the change would otherwise break the dependency rules.

## Adding a module

- Apply the project's convention plugins (for example `id("<project>.android.feature")`) instead of copying `android {}` blocks. If no convention plugin fits, follow the closest existing module.
- Declare dependencies through the version catalog. Add a catalog entry rather than a hardcoded coordinate.
- Register the module in `settings.gradle.kts` and add the `impl` to `:app`'s dependencies.
- Set a unique `namespace` following the existing scheme.
