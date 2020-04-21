# BlinkID SDK Flutter plugin

This repository contains example plugin for BlinkID native SDKs ([iOS](https://github.com/BlinkID/blinkid-ios)
and [Android](https://github.com/BlinkID/blinkid-android)). Plugin currently supports only `BlinkIdCombinedRecognizer`.
For 100% of features and maximum control, consider using native SDK.

## Requirements
BlinkID plugin is developed with Flutter SDK version 1.12.13.
For help with Flutter, view official [documentation](https://flutter.dev/docs).

## Getting Started
To get started first clone the repository:
```shell
git clone https://github.com/BlinkID/blinkid-flutter.git
```

To run sample application use following commands:
```shell
cd sample/
flutter run
```
If there are problems with running the application, please make sure you have
properly configured tools by running `flutter doctor`. You can also try running
application from Android Studio.

### Plugin code
Plugin code can be found in folder `lib`, while platform specific implementation is
folder `android` and `ios`.

#### Android
Android specific implementation is in folder `android`. It contains Java code for
performing scan. `BlinkIdPlugin.java` is main file with scanning logic, it uses
`BlinkIdCombinedRecognizer`. For easier scan results extraction, two utils files are given:
`BlinkIdCombinedRecognizerSerialization` and `SerializationUtils`.

#### iOS
iOS specific implementation is currently **in development** and is not available.

### Sample application
To try BlinkID plugin, there is a minimal sample application in `sample` folder.
Sample currently works only on Android and can be run on devices with
Android 4.1 version (API level 16) or higher.

### Licensing
- [Generate](https://microblink.com/login?url=/customer/generatedemolicence) a **free demo license key** to start using the SDK in your app (registration required)
- Get information about pricing and licensing of [BlinkID](https://microblink.com/blinkid)
