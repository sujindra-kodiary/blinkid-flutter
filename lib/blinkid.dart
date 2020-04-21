import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class BlinkId {
  static const MethodChannel _channel = const MethodChannel('blinkid');

  static Future<Map<String, dynamic>> scan(license) async {
    return jsonDecode(await _channel.invokeMethod('scan', {"license": license}));
  }
}
