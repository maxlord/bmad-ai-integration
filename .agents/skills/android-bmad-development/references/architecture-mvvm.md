# MVVM with unidirectional data flow

Apply these rules when the project already uses ViewModels. Match existing naming and base classes where they differ from this guide.

## Layers

- **UI** (`@Composable` screens, or fragments/views): renders state and forwards user intent. No business logic, no repository access.
- **ViewModel**: owns screen state, turns user intent into data-layer calls, and maps data to UI state.
- **Domain (optional)**: add a use case only when logic is reused by several ViewModels or combines several repositories. Otherwise the ViewModel calls the repository directly. Do not create pass-through use cases.
- **Data**: repositories are the single source of truth. They expose `Flow` for observable data and `suspend` functions for one-shot operations, and hide data sources behind an interface.

## Screen state

- Model state as one immutable type per screen, usually a `sealed interface` (`Loading`, `Success`, `Error`) or a `data class` with explicit fields. Prefer whichever the neighboring screens use.
- Expose it as `StateFlow<UiState>` and never expose a `MutableStateFlow`.
- Derive state from repository flows with `combine`/`map` and `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue)`. Use a private `MutableStateFlow` only for state the ViewModel alone owns, such as input fields.
- UI-only state that does not survive the screen or matter to business logic, such as a dropdown's expanded flag, stays in the composable with `remember`/`rememberSaveable`.

## User intent and one-off effects

- Expose intent as plain functions (`fun onRetry()`, `fun onQueryChange(value: String)`), or a single `onAction(action)` when the module already uses that style.
- Represent results the UI must react to, such as navigation after save or a snackbar message, as state (`val savedSuccessfully: Boolean`, `val userMessage: Message?`) that the UI consumes and acknowledges through a ViewModel call. Use `Channel`/`SharedFlow` event streams only when the project already does.

## Compose boundary

Split each screen into two composables:

```kotlin
@Composable
internal fun ProfileRoute(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProfileScreen(uiState = uiState, onRetry = viewModel::onRetry, onBack = onBack)
}

@Composable
internal fun ProfileScreen(
    uiState: ProfileUiState,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
)
```

- The route composable connects the ViewModel. The screen composable is stateless, previewable, and the target of Compose UI tests.
- Collect flows with `collectAsStateWithLifecycle()`, not `collectAsState()`.
- Never pass a ViewModel into child composables. Pass state and lambdas.

## Threading and errors

- Launch work in `viewModelScope`. Repositories switch dispatchers themselves (an injected `CoroutineDispatcher`, not a hardcoded `Dispatchers.IO`), so ViewModels stay main-safe and testable.
- Map failures to UI state at the ViewModel boundary. Do not let exceptions from data sources crash collection, and do not swallow `CancellationException`.
- Use `SavedStateHandle` for navigation arguments and small state that must survive process death.
