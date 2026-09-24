# Hilt

Apply these rules when the project already uses Hilt. Follow the project's existing annotation processor (KSP or kapt) and do not migrate it as part of an unrelated change.

## Entry points

- The application class carries `@HiltAndroidApp`. Activities and fragments that obtain dependencies carry `@AndroidEntryPoint`.
- ViewModels use `@HiltViewModel` with `@Inject constructor`, and are obtained in Compose with `hiltViewModel()`. For assisted parameters, use `@AssistedInject` with `@HiltViewModel(assistedFactory = ...)` only when navigation arguments through `SavedStateHandle` are insufficient.

## Bindings

- Prefer constructor injection (`class DefaultUserRepository @Inject constructor(...)`). Write a module only for interfaces, third-party types, or configuration.
- Bind interfaces with `@Binds` in an `abstract` module. Use `@Provides` in an `object` module for types you cannot construct yourself, such as Retrofit, Room, or DataStore.
- Install into the narrowest component that matches the lifetime: `SingletonComponent` for app-wide data sources, `ViewModelComponent` for dependencies scoped to one ViewModel, and `ActivityComponent`/`ActivityRetainedComponent` only for activity-bound needs.
- Scope annotations (`@Singleton`, `@ViewModelScoped`) are for shared state or expensive objects. An unscoped binding is the default.
- Distinguish same-type bindings with qualifiers defined in one shared place, such as `@Dispatcher(IO)`, not with `@Named` strings scattered across modules.
- Inject `CoroutineDispatcher` and an application `CoroutineScope` through qualifiers so tests can replace them.

## Hilt across api/impl modules

- Put the interface in the feature's `api` module and the implementation plus its `@Binds` module in `impl`. The `api` module does not depend on Hilt.
- `:app` depends on every `impl` module, so Hilt aggregates their modules. A binding that exists only in an `impl` module not reachable from `:app` fails at compile time with a missing-binding error. Check the `:app` dependency list before assuming a Hilt bug.
- Keep modules and bindings `internal` in `impl` where Kotlin visibility allows. Hilt modules must be visible to the generated code, so follow the project's existing visibility for modules.

## Testing with Hilt

- ViewModel unit tests construct the ViewModel directly with fakes. They do not start Hilt.
- Instrumented tests that need the real graph use `@HiltAndroidTest`, a `HiltAndroidRule` ordered before the Compose rule, and the project's Hilt test runner, which supplies `HiltTestApplication`.
- Replace production modules with `@TestInstallIn(components = [SingletonComponent::class], replaces = [DataModule::class])` in shared test code, or `@UninstallModules` plus `@BindValue` for a single test class. Prefer the project's existing approach.
