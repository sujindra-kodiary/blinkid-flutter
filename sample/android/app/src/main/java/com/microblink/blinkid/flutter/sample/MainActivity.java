package com.microblink.blinkid.flutter.sample;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import com.microblink.blinkid.BlinkIdPlugin;

public class MainActivity extends FlutterActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    BlinkIdPlugin.registerWith(registrarFor("com.microblink.blinkid.BlinkIdPlugin"));
  }
}
