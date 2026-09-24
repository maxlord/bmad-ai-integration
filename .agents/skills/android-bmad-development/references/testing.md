# Testing MVVM, Hilt, and multi-module code

Default stack: JUnit 4, hand-written fakes, Turbine for flows, `kotlinx-coroutines-test`, Compose UI tests, and Hilt instrumented tests where the real graph matters. Follow the project's existing tools if they differ.

## Choosing the test

| Changed behavior | Test | Location |
| --- | --- | --- |
| ViewModel state transitions or intent handling | Unit test with fake repositories | `:feature:<name>:impl` `src/test` |
| Use case or mapping logic | Unit test | owning module `src/test` |
| Repository combining data sources | Unit test with fake data sources; Room tests instrumented | owning data module |
| Stateless screen rendering and interaction | Compose UI test against the screen composable | `:feature:<name>:impl` `src/androidTest` |
| Navigation or wiring across features | Hilt instrumented test | `:app` `src/androidTest` |

Test the stateless screen composable with plain state and lambdas. Reserve Hilt instrumented tests for behavior that depends on the real graph.

## Fakes

- Implement repository interfaces with fakes in `:core:testing`, or in the feature's shared test code when only that feature uses them.
- Back observable fakes with a `MutableStateFlow` or `MutableSharedFlow`, and give them methods for tests to emit data and failures.
- Use a mocking library only when the project already does and a fake would be disproportionate.

## Coroutines and flows

- Replace `Dispatchers.Main` with a `MainDispatcherRule` from `:core:testing` or its project equivalent.
- Run tests in `runTest`. Inject a `TestDispatcher` wherever production code receives a dispatcher.
- Assert flows with Turbine, for example `viewModel.uiState.test { assertEquals(Loading, awaitItem()) }`. When state uses `WhileSubscribed`, collect it for the whole assertion; otherwise the upstream never starts.
- Do not rely on `delay` or `Thread.sleep` for ordering. Advance the scheduler explicitly.

## Gradle commands

Run the narrowest tasks that cover the change, then widen when a public contract changed:

```bash
./gradlew :feature:<name>:impl:testDebugUnitTest :feature:<name>:impl:lintDebug
./gradlew :app:assembleDebug                         # after api, dependency, or Hilt module changes
./gradlew :feature:<name>:impl:connectedDebugAndroidTest   # requires an emulator or device
./gradlew :app:connectedDebugAndroidTest                   # Hilt and navigation tests
```

Check the project's build variants first; replace `Debug` when the project uses flavors. A Hilt missing-binding error or a dependency-rule violation only appears when building `:app`, so do not skip that build after changing bindings or module dependencies.
