package com.microblink.flutter.overlays

import android.content.Context
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.uisettings.UISettings
import com.microblink.flutter.overlays.serialization.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import kotlin.jvm.Throws

enum class OverlaySettingsSerializers {
    INSTANCE;

    private val mByJSONName: HashMap<String, OverlaySettingsSerialization> = HashMap()
    private fun registerMapping(overlaySettingsSerialization: OverlaySettingsSerialization) {
        mByJSONName.put(overlaySettingsSerialization.getJsonName(), overlaySettingsSerialization)
    }

    fun getOverlaySettings(context: Context?, jsonOverlaySettings: JSONObject, recognizerBundle: RecognizerBundle?): UISettings {
        return try {
            mByJSONName.get(jsonOverlaySettings.getString("overlaySettingsType")).createUISettings(context, jsonOverlaySettings, recognizerBundle)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }

    init {
        registerMapping(DocumentOverlaySettingsSerialization())
        registerMapping(DocumentVerificationOverlaySettingsSerialization())
        registerMapping(BlinkIdOverlaySettingsSerialization())
    }
}