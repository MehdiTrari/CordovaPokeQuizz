This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## Maestro UI tests

The project now ships two Maestro flows that validate the Pokemon quiz scenarios while mocking the remote API through Maestro's built-in proxy.

1. Install Maestro (see [the official docs](https://maestro.mobile.dev/)) and ensure an Android emulator or device is available.
2. Build and install the debug app: `./gradlew :composeApp:installDebug`.
3. Run the happy-path flow: `maestro test maestro/flows/pokemon_success.yaml`.
4. Run the wrong-answer flow: `maestro test maestro/flows/pokemon_failure.yaml`.

Each flow starts a Maestro proxy that intercepts calls to `https://tyradex.vercel.app/api/v1/pokemon/*` and returns a fixed Pikachu payload so the UI is deterministic. The first flow types the correct answer and asserts that the label and score update, while the second enters a wrong answer and verifies that the score remains unchanged.
