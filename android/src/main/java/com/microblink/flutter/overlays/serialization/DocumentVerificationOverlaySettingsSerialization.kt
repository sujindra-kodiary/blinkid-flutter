package com.microblink.flutter.overlays.serialization

import android.content.Context
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.fragment.overlay.blinkid.documentverification.DocumentVerificationOverlayStrings
import com.microblink.uisettings.DocumentVerificationUISettings
import com.microblink.uisettings.UISettings
import com.microblink.flutter.overlays.OverlaySettingsSerialization
import org.json.JSONObject
import com.microblink.flutter.SerializationUtils.getStringFromJSONObject
import kotlin.jvm.Throws

class DocumentVerificationOverlaySettingsSerialization : OverlaySettingsSerialization {
    @Override
    override fun createUISettings(context: Context?, jsonUISettings: JSONObject, recognizerBundle: RecognizerBundle?): UISettings {
        val settings = DocumentVerificationUISettings(recognizerBundle)
        val overlasStringsBuilder: DocumentVerificationOverlayStrings.Builder = Builder(context)
        val firstSideSplashMessage: String = getStringFromJSONObject(jsonUISettings, "firstSideSplashMessage")
        if (firstSideSplashMessage != null) {
            overlasStringsBuilder.setFrontSideSplashText(firstSideSplashMessage)
        }
        val secondSideSplashMessage: String = getStringFromJSONObject(jsonUISettings, "secondSideSplashMessage")
        if (secondSideSplashMessage != null) {
            overlasStringsBuilder.setBackSideSplashText(secondSideSplashMessage)
        }
        val firstSideInstructions: String = getStringFromJSONObject(jsonUISettings, "firstSideInstructions")
        if (firstSideInstructions != null) {
            overlasStringsBuilder.setFrontSideInstructions(firstSideInstructions)
        }
        val secondSideInstructions: String = getStringFromJSONObject(jsonUISettings, "secondSideInstructions")
        if (secondSideInstructions != null) {
            overlasStringsBuilder.setBackSideInstructions(secondSideInstructions)
        }
        val glareMessage: String = getStringFromJSONObject(jsonUISettings, "glareMessage")
        if (glareMessage != null) {
            overlasStringsBuilder.setGlareMessage(glareMessage)
        }
        settings.setStrings(overlasStringsBuilder.build())
        return settings
    }

    @get:Override
    override val jsonName: String?
        get() = "DocumentVerificationOverlaySettings"
}