# Product brief: BMAD Android demo

## Audience and problem

A developer or product practitioner arrives at this repository without prior BMAD Method experience. They need to understand what BMAD helps with, which skills matter for their role, how to add it to a project, and whether the approach can produce a working application.

## Outcome

Within a few minutes, a visitor can explain the path from idea to implementation, locate a relevant BMAD skill, install BMAD for Codex in another repository, and build and run this Android example.

## Demonstration

The app has one screen. It initially shows “Tap the button” above a “Say hello” button. Tapping the button changes the text to “Hello World”. The simplicity keeps attention on the development workflow rather than app features.

## Success checks

- The README distinguishes BMAD's agent workflows from Android runtime code and from GitHub merge automation.
- The project contains committed BMAD configuration and skills, plus an original Android-focused skill.
- `./gradlew :app:assembleDebug` succeeds, and a UI test proves the button interaction.
