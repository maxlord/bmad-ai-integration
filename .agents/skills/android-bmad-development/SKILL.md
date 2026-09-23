---
name: android-bmad-development
description: Implement and verify a feature or fix in an existing Android Kotlin project using BMAD project context. Use for Android code changes, especially Jetpack Compose UI work. Do not use for general BMAD planning or non-Android projects.
---

# Android development with BMAD context

Use this skill after the task's intent is clear. BMAD's product, UX, specification, and review skills remain available for those distinct jobs; this skill supplies Android-specific implementation judgment.

1. Read the project's Android build files, relevant source and tests, and any BMAD project context or specification that applies. Identify the module, UI technology, minimum SDK, and existing conventions before changing code.
2. State the user-visible acceptance behavior in one or two sentences. For unclear product behavior, use the relevant BMAD planning skill or ask the user; do not invent a broader feature.
3. Make the smallest cohesive Kotlin/Android change. Follow the project's existing architecture and UI toolkit. For Compose, keep state at the narrowest useful owner, expose a testable UI boundary, and avoid adding dependencies or layers for trivial behavior.
4. Add or adjust a test at the level that proves the changed behavior. Prefer a Compose UI test for interactions, a unit test for isolated logic, and an integration test only when components must work together. Do not add tests that merely repeat implementation details.
5. Run the relevant Gradle task and Android lint. If a device is available, run the UI test. If not, report the exact unrun check and how to run it. Fix failures caused by the change.
6. Summarize the behavior, verification, and any remaining limitation. For a BMAD workflow, pass the result to `bmad-code-review` when review is requested or part of the active process.

This skill never merges branches or publishes changes by itself. Those actions follow the user's repository workflow and authorization.
