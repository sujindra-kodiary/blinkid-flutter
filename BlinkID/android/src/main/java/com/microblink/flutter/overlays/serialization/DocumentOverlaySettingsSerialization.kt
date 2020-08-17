package com.microblink.flutter.overlays.serialization

import android.content.Context
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.uisettings.DocumentUISettings
import com.microblink.uisettings.UISettings
import com.microblink.flutter.overlays.OverlaySettingsSerialization
import org.json.JSONObject
import kotlin.jvm.Throws

class DocumentOverlaySettingsSerialization : OverlaySettingsSerialization {
    @Override
    override fun createUISettings(context: Context?, jsonUISettings: JSONObject?, recognizerBundle: RecognizerBundle?): UISettings {
        // no settings deserialized at the moment
        return DocumentUISettings(recognizerBundle)
    }

    @get:Override
    override val jsonName: String?
        get() = "DocumentOverlaySettings"
}