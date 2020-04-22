import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class BlinkId {
  static const MethodChannel _channel = const MethodChannel('blinkid');

  static const String SCAN_METHOD = 'scan';
  static const String ARG_LICENSE = 'license';
  static const String SCAN_CANCELLED = 'cancelled';
  static const String SCAN_RESULT = 'result';

  static Future<Map<String, dynamic>> scan(license) async {
    return jsonDecode(await _channel.invokeMethod(SCAN_METHOD, {ARG_LICENSE: license}));
  }

  static bool isResultValid(result) {
    // Empty => 1
    // Uncertain => 2
    // Valid => 3
    return result["resultState"] == 3;
  }
}
