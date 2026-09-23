# Project context

- Product: a small, public teaching repository for BMAD Method on Android.
- Audience: newcomers to BMAD, including product, design, development, and QA roles.
- Language: English for public documentation and BMAD-generated documents.
- Android stack: one Kotlin app module, Jetpack Compose, Material 3, Gradle Kotlin DSL.
- UI contract: initial text “Tap the button”; button “Say hello”; after a tap, text “Hello World”.
- Quality bar: keep the app easy to understand, build with the checked-in Gradle wrapper, run lint, and verify interaction with a Compose UI test.
- BMAD scope: `_bmad/` and the generated `bmad-*` skills come from BMAD Method 6.12.0. The `android-bmad-development` skill is an original project addition and must survive BMAD updates.
- Repository workflow: feature branches start from `dev`; review and CI should pass before a human decides to merge. BMAD alone does not configure GitHub auto-merge.
