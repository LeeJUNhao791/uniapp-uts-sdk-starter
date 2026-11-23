# Contributing

Issues and pull requests are welcome in English or Chinese. Include the
revision, HBuilderX version, Android/device version, reproduction steps, and
expected versus actual callback behavior. Keep public reports free of private AppIDs,
credentials, and private vendor SDK artifacts.

Keep pull requests focused and update both README files when usage changes.
Describe Java checks and Android/UTS device checks separately.

## Build and Java checks

Install JDK 17 and Android SDK Platform 35. Set `JAVA_HOME` and `ANDROID_HOME`,
then run from the repository root.

Windows PowerShell:

```powershell
.\android-sdk\gradlew.bat -p android-sdk --no-daemon verifyDemoScanner stageDemoAar :text-check:verifyTextCheck :text-check:assembleRelease
```

macOS / Linux:

```sh
./android-sdk/gradlew -p android-sdk --no-daemon verifyDemoScanner stageDemoAar :text-check:verifyTextCheck :text-check:assembleRelease
```

The scanner AAR is staged into the demo's ignored `libs/` directory.
[Android AAR CI](.github/workflows/android-aar.yml) also compares its bytes
and checks that the public demo AppID is empty. Leave local HBuilderX settings,
`unpackage/`, and generated AARs untracked.

For UTS or page changes, follow the [Android quickstart](docs/android-quickstart.md)
and record the callback and lifecycle cases you ran on a device.
