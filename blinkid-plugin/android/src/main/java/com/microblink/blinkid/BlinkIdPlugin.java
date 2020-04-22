package com.microblink.blinkid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.microblink.MicroblinkSDK;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.blinkid.generic.*;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.intent.IntentDataTransferMode;
import com.microblink.uisettings.UISettings;
import com.microblink.uisettings.BlinkIdUISettings;

import org.json.JSONException;
import org.json.JSONObject;


public class BlinkIdPlugin implements FlutterPlugin, MethodCallHandler, PluginRegistry.ActivityResultListener {

  private static final int REQ_CODE = 1904;

  private static final String CHANNEL = "blinkid";

  private static final String SCAN_METHOD = "scan";
  private static final String ARG_LICENSE = "license";
  private static final String SCAN_CANCELLED = "cancelled";
  private static final String SCAN_RESULT = "result";

  private RecognizerBundle recognizerBundle;

  private MethodChannel channel;
  private Context context;

  private Result pendingResult;

  public static void registerWith(Registrar registrar) {
    final BlinkIdPlugin plugin = new BlinkIdPlugin();
    plugin.setupPlugin(registrar.activity(), registrar.messenger());
    registrar.addActivityResultListener(plugin);
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    setupPlugin(
            binding.getApplicationContext(),
            binding.getBinaryMessenger()
    );
  }

  private void setupPlugin(Context context, BinaryMessenger messenger) {
    if (context != null) {
      this.context = context;
    }

    this.channel = new MethodChannel(messenger, CHANNEL);
    this.channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals(SCAN_METHOD)) {
      this.pendingResult = result;

      MicroblinkSDK.setLicenseKey((String) call.argument(ARG_LICENSE), context);
      MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);

      BlinkIdCombinedRecognizer recognizer = new BlinkIdCombinedRecognizer();
      recognizer.setReturnFullDocumentImage(true);
      recognizer.setReturnFaceImage(true);

      recognizerBundle = new RecognizerBundle(recognizer);
      BlinkIdUISettings uiSettings = new BlinkIdUISettings(recognizerBundle);

      Intent intent = new Intent(context, uiSettings.getTargetActivity());
      uiSettings.saveToIntent(intent);
      ((Activity) context).startActivityForResult(intent, REQ_CODE);

    } else {
      result.notImplemented();
    }
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    this.context = null;

    this.channel.setMethodCallHandler(null);
    this.channel = null;
  }


  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQ_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        // load bundle
        recognizerBundle.loadFromIntent(data);
        JSONObject result = new JSONObject();
        try {
          result.put(SCAN_CANCELLED, false);
          result.put(
                  SCAN_RESULT,
                  BlinkIdCombinedRecognizerSerialization.serializeResult(
                          recognizerBundle.getRecognizers()[0]
                  )
          );
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }

        pendingResult.success(result.toString());

      } else if (resultCode == Activity.RESULT_CANCELED) {
        JSONObject result = new JSONObject();
        try {
          result.put(SCAN_CANCELLED, true);
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }

        pendingResult.success(result.toString());

      } else {
        pendingResult.error("Unexpected error", null, null);
      }

      pendingResult = null;
      return true;
    }

    return false;
  }
}
