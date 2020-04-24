# BlinkID SDK Flutter plugin

This repository contains example plugin for BlinkID native SDKs ([iOS](https://github.com/BlinkID/blinkid-ios)
and [Android](https://github.com/BlinkID/blinkid-android)). 

> :warning: This is a beta version of the plugin. Plugin currently supports only `BlinkIdCombinedRecognizer`, other features are not available. For 100% of features and maximum control, consider using native SDK.

## Requirements
BlinkID plugin is developed with Flutter SDK version 1.12.13.
For help with Flutter, view official [documentation](https://flutter.dev/docs).

## Getting Started
To get started, first clone the repository:
```shell
git clone https://github.com/BlinkID/blinkid-flutter.git
```

### Quick start with sample app
To try BlinkID plugin, there is a minimal sample application in `sample` folder.
Sample currently works only on Android and can be run on devices with
Android 4.1 version (API level 16) or higher.

To run sample application, use the following commands:
```shell
cd sample/
flutter run
```
If there are problems with running the application, please make sure you have
properly configured tools by running `flutter doctor`. You can also try running
the application from Android Studio.

### Plugin integration
1. Integrate BlinkId plugin in your application by setting path to the cloned repository
in your `pubspec.yaml`:
```yaml
dev_dependencies:
  ...
  blinkid:
    path: path/to/blinkid-plugin
```

2. Perform scan by calling method `BlinkId.scan()` with your license key. To find out more about licensing, click
 [here](#licensing).
```dart
Future<void> scan() async {
    Map<String, dynamic> results;

    try {
      // set your license here
      String license = "";

      // perform scan and gather results
      results = await BlinkId.scan(license);

    } on PlatformException {
      // handle exception
    }
```

3. When scanning is completed, variable `results` will contain scan related data. If the scanning was
cancelled, `results[BlinkId.SCAN_CANCELLED]` will be set to `true`. Otherwise, it will be `false` and you
can retrieve the data with `results[BlinkId.SCAN_RESULT]`. This will return a map with values
representing scanned data. The map can be passed to method `BlinkId.isResultValid()` to check if the
results are valid.

For more information please refer to our sample application source code.

## Plugin code
Plugin code can be found in folder `blinkid-plugin`. Plugin implementation is in folder `lib`,
while platform specific implementations are in `android` and `ios` folders.

### Android
Android specific implementation is in folder `blinkid-plugin/android`. It contains Java code for
performing a scan. `BlinkIdPlugin.java` is the main file with scanning logic, it uses
`BlinkIdCombinedRecognizer`. For easier scan results extraction, two utils files are given:
`BlinkIdCombinedRecognizerSerialization` and `SerializationUtils`.

### iOS
iOS specific implementation is currently **in development** and is not available.

## Licensing
- [Generate](https://microblink.com/login?url=/customer/generatedemolicence) a **free demo license key** to start using the SDK in your app (registration required)
- Get information about pricing and licensing of [BlinkID](https://microblink.com/blinkid)
