package com.microblink.flutter.overlays

import android.content.Context
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.uisettings.UISettings
import org.json.JSONObject
import kotlin.jvm.Throws

interface OverlaySettingsSerialization {
    fun createUISettings(context: Context?, jsonUISettings: JSONObject?, recognizerBundle: RecognizerBundle?): UISettings?
    val jsonName: String?
}