import "dart:convert";

import "package:flutter/material.dart";
import "dart:async";

import "package:flutter/services.dart";
import "package:blinkid/blinkid.dart";

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Map<String, dynamic> _scanResult;
  String _scanResultString = "";
  String _fullDocumentFrontImageBase64 = "";
  String _fullDocumentBackImageBase64 = "";
  String _faceImageBase64 = "";

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> scan() async {
    Map<String, dynamic> results;

    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      // Set your license here
      String license = "sRwAAAAlY29tLm1pY3JvYmxpbmsuYmxpbmtpZC5mbHV0dGVyLnNhbXBsZRZ6oiXEI76P+UYnfRsA4+gCFj8ifJdPjtPehAYJD8P2/vPMcqjblVsjKDt10dUM9W5UxEg+Dug5jlMhzwUDVRwa6KjNQ5SaGYEJDlzL8cegfVJSHtf4NzqFfyzl3UwCFIUQYGxfZpkX2fuJQqsl6Ch9MJQ9BiOcJJW83bfaNeHLDSxFblIVoD7W5vuw8o4mz8pC52ZGfspUKe65mgx3eAWrO6jPe15rsvIg42UUUMQSmk8Pvx3NfyR94gakhK3OX3EGzDkrZFqg78KRdlwFzA==";
      results = await BlinkId.scan(license);
    } on PlatformException {
      // handle exception
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    if (results != null && results[BlinkId.SCAN_CANCELLED] == false) {
      setState(() {
        _scanResult = results[BlinkId.SCAN_RESULT];
        _scanResultString = buildResultString();
        _fullDocumentFrontImageBase64 =
            getEncodedImage("fullDocumentFrontImage");
        _fullDocumentBackImageBase64 = getEncodedImage("fullDocumentBackImage");
        _faceImageBase64 = getEncodedImage("faceImage");
      });
    }
  }

  String getEncodedImage(String imageTitle) {
    if (isResultValid()) {
      return _scanResult[imageTitle];
    }
    return "";
  }

  String buildResultString() {
    String resultString = "";

    if (isResultValid()) {
      resultString = "First name: ${_scanResult["firstName"]}\n"
          "Last name: ${_scanResult["lastName"]}\n"
          "Address: ${_scanResult["address"]}\n"
          "Document number: ${_scanResult["documentNumber"]}\n"
          "Sex: ${_scanResult["sex"]}\n";

      if (_scanResult.containsKey("dateOfBirth")) {
        resultString += "Date of birth: ${_scanResult["dateOfBirth"]["day"]}."
            "${_scanResult["dateOfBirth"]["month"]}."
            "${_scanResult["dateOfBirth"]["year"]}\n";
      }

      if (_scanResult.containsKey("dateOfIssue")) {
        resultString += "Date of issue: ${_scanResult["dateOfIssue"]["day"]}."
            "${_scanResult["dateOfIssue"]["month"]}."
            "${_scanResult["dateOfIssue"]["year"]}\n";
      }

      if (_scanResult.containsKey("dateOfExpiry")) {
        resultString += "Date of expiry: ${_scanResult["dateOfExpiry"]["day"]}."
            "${_scanResult["dateOfExpiry"]["month"]}."
            "${_scanResult["dateOfExpiry"]["year"]}\n";
      }
    } else {
      resultString = "Result is empty!";
    }

    return resultString;
  }

  bool isResultValid() {
    // Empty => 1
    // Uncertain => 2
    // Valid => 3
    return _scanResult["resultState"] == 3;
  }

  @override
  Widget build(BuildContext context) {
    Widget fullDocumentFrontImage = Container();
    if (_fullDocumentFrontImageBase64 != null &&
        _fullDocumentFrontImageBase64 != "") {
      fullDocumentFrontImage = Column(
        children: <Widget>[
          Text("Document Front Image:"),
          Image.memory(
            Base64Decoder().convert(_fullDocumentFrontImageBase64),
            height: 180,
            width: 350,
          )
        ],
      );
    }

    Widget fullDocumentBackImage = Container();
    if (_fullDocumentBackImageBase64 != null &&
        _fullDocumentBackImageBase64 != "") {
      fullDocumentBackImage = Column(
        children: <Widget>[
          Text("Document Back Image:"),
          Image.memory(
            Base64Decoder().convert(_fullDocumentBackImageBase64),
            height: 180,
            width: 350,
          )
        ],
      );
    }

    Widget faceImage = Container();
    if (_faceImageBase64 != null && _faceImageBase64 != "") {
      faceImage = Column(
        children: <Widget>[
          Text("Face Image:"),
          Image.memory(
            Base64Decoder().convert(_faceImageBase64),
            height: 150,
            width: 100,
          )
        ],
      );
    }

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text("BlinkID Demo"),
        ),
        body: SingleChildScrollView(
            padding: EdgeInsets.all(16.0),
            child: Column(
              children: <Widget>[
                Padding(
                    child: RaisedButton(
                      child: Text("Scan"),
                      onPressed: () => scan(),
                    ),
                    padding: EdgeInsets.only(bottom: 16.0)),
                Text(_scanResultString),
                fullDocumentFrontImage,
                fullDocumentBackImage,
                faceImage,
              ],
            )),
      ));
  }
}
