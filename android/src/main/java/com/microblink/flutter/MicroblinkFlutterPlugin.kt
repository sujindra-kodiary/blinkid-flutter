package com.microblink.flutter

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import com.microblink.MicroblinkSDK
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.generic.*
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.intent.IntentDataTransferMode
import com.microblink.uisettings.UISettings
import com.microblink.uisettings.BlinkIdUISettings
import com.microblink.uisettings.ActivityRunner
import com.microblink.flutter.recognizers.RecognizerSerializers
import com.microblink.flutter.overlays.OverlaySettingsSerializers
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray
import kotlin.jvm.Throws

class MicroblinkFlutterPlugin : FlutterPlugin, MethodCallHandler, PluginRegistry.ActivityResultListener {
    private var mRecognizerBundle: RecognizerBundle? = null
    private var channel: MethodChannel? = null
    private var context: Context? = null
    private var pendingResult: Result? = null

    @Override
    fun onAttachedToEngine(@NonNull binding: FlutterPluginBinding?) {
        // not used
    }

    private fun setupPlugin(context: Context?, messenger: BinaryMessenger) {
        if (context != null) {
            this.context = context
        }
        channel = MethodChannel(messenger, CHANNEL)
        channel.setMethodCallHandler(this)
    }

    @Override
    fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        setLicense(call.argument(ARG_LICENSE) as Map)
        if (call.method.equals(METHOD_SCAN)) {
            pendingResult = result
            val jsonOverlaySettings = JSONObject(call.argument(ARG_OVERLAY_SETTINGS) as Map)
            val jsonRecognizerCollection = JSONObject(call.argument(ARG_RECOGNIZER_COLLECTION) as Map)
            mRecognizerBundle = RecognizerSerializers.INSTANCE.deserializeRecognizerCollection(jsonRecognizerCollection)
            val uiSettings: UISettings = OverlaySettingsSerializers.INSTANCE.getOverlaySettings(context, jsonOverlaySettings, mRecognizerBundle)
            startScanning(context, SCAN_REQ_CODE, uiSettings)
        } else {
            result.notImplemented()
        }
    }

    @SuppressWarnings("unchecked")
    private fun setLicense(licenseMap: Map) {
        MicroblinkSDK.setShowTimeLimitedLicenseWarning(licenseMap.getOrDefault(ARG_SHOW_LICENSE_WARNING, true) as Boolean)
        val licenseKey = licenseMap.get(ARG_LICENSE_KEY) as String
        val licensee = licenseMap.getOrDefault(ARG_LICENSEE, null) as String
        if (licensee == null) {
            MicroblinkSDK.setLicenseKey(licenseKey, context)
        } else {
            MicroblinkSDK.setLicenseKey(licenseKey, licensee, context)
        }
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED)
    }

    private fun startScanning(context: Context?, requestCode: Int, uiSettings: UISettings) {
        if (context is Activity) {
            ActivityRunner.startActivityForResult(context as Activity?, requestCode, uiSettings)
        } else {
            pendingResult.error("Context can't be casted to Activity", null, null)
        }
    }

    @Override
    fun onDetachedFromEngine(@NonNull binding: FlutterPluginBinding?) {
        context = null
        channel.setMethodCallHandler(null)
        channel = null
    }

    @Override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SCAN_REQ_CODE) {
                mRecognizerBundle.loadFromIntent(data)
                val resultList: JSONArray = RecognizerSerializers.INSTANCE.serializeRecognizerResults(mRecognizerBundle.getRecognizers())
                pendingResult.success(resultList.toString())
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            pendingResult.success("null")
        } else {
            pendingResult.error("Unexpected error", null, null)
        }
        pendingResult = null
        return true
    }

    companion object {
        private const val CHANNEL = "microblink_scanner"
        private const val SCAN_REQ_CODE = 1904
        private const val METHOD_SCAN = "scanWithCamera"
        private const val ARG_LICENSE = "license"
        private const val ARG_LICENSE_KEY = "licenseKey"
        private const val ARG_LICENSEE = "licensee"
        private const val ARG_SHOW_LICENSE_WARNING = "showTimeLimitedLicenseKeyWarning"
        private const val ARG_RECOGNIZER_COLLECTION = "recognizerCollection"
        private const val ARG_OVERLAY_SETTINGS = "overlaySettings"
        fun registerWith(registrar: Registrar) {
            val plugin = MicroblinkFlutterPlugin()
            plugin.setupPlugin(registrar.activity(), registrar.messenger())
            registrar.addActivityResultListener(plugin)
        }
    }
}