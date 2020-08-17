package com.microblink.flutter.overlays.serialization

import android.content.Context
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.fragment.overlay.blinkid.reticleui.ReticleOverlayStrings
import com.microblink.uisettings.BlinkIdUISettings
import com.microblink.uisettings.UISettings
import com.microblink.flutter.overlays.OverlaySettingsSerialization
import com.microblink.flutter.SerializationUtils
import org.json.JSONObject
import com.microblink.flutter.SerializationUtils.getStringFromJSONObject
import kotlin.jvm.Throws

class BlinkIdOverlaySettingsSerialization : OverlaySettingsSerialization {
    @Override
    override fun createUISettings(context: Context?, jsonUISettings: JSONObject, recognizerBundle: RecognizerBundle?): UISettings {
        val settings = BlinkIdUISettings(recognizerBundle)
        val requireDocumentSidesDataMatch: Boolean = jsonUISettings.optBoolean("requireDocumentSidesDataMatch", true)
        settings.setDocumentDataMatchRequired(requireDocumentSidesDataMatch)
        val showNotSupportedDialog: Boolean = jsonUISettings.optBoolean("showNotSupportedDialog", true)
        settings.setShowNotSupportedDialog(showNotSupportedDialog)
        val backSideScanningTimeoutMilliseconds: Long = jsonUISettings.optLong("backSideScanningTimeoutMilliseconds", 17000)
        settings.setBackSideScanningTimeoutMs(backSideScanningTimeoutMilliseconds)
        val overlasStringsBuilder: ReticleOverlayStrings.Builder = Builder(context)
        val firstSideInstructionsText: String = getStringFromJSONObject(jsonUISettings, "firstSideInstructionsText")
        if (firstSideInstructionsText != null) {
            overlasStringsBuilder.setFirstSideInstructionsText(firstSideInstructionsText)
        }
        val flipInstructions: String = getStringFromJSONObject(jsonUISettings, "flipInstructions")
        if (flipInstructions != null) {
            overlasStringsBuilder.setFlipInstructions(flipInstructions)
        }
        val errorMoveCloser: String = getStringFromJSONObject(jsonUISettings, "errorMoveCloser")
        if (errorMoveCloser != null) {
            overlasStringsBuilder.setErrorMoveCloser(errorMoveCloser)
        }
        val errorMoveFarther: String = getStringFromJSONObject(jsonUISettings, "errorMoveFarther")
        if (errorMoveFarther != null) {
            overlasStringsBuilder.setErrorMoveFarther(errorMoveFarther)
        }
        val sidesNotMatchingTitle: String = getStringFromJSONObject(jsonUISettings, "sidesNotMatchingTitle")
        if (sidesNotMatchingTitle != null) {
            overlasStringsBuilder.setSidesNotMatchingTitle(sidesNotMatchingTitle)
        }
        val sidesNotMatchingMessage: String = getStringFromJSONObject(jsonUISettings, "sidesNotMatchingMessage")
        if (sidesNotMatchingMessage != null) {
            overlasStringsBuilder.setSidesNotMatchingMessage(sidesNotMatchingMessage)
        }
        val unsupportedDocumentTitle: String = getStringFromJSONObject(jsonUISettings, "unsupportedDocumentTitle")
        if (unsupportedDocumentTitle != null) {
            overlasStringsBuilder.setUnsupportedDocumentTitle(unsupportedDocumentTitle)
        }
        val unsupportedDocumentMessage: String = getStringFromJSONObject(jsonUISettings, "unsupportedDocumentMessage")
        if (unsupportedDocumentMessage != null) {
            overlasStringsBuilder.setUnsupportedDocumentMessage(unsupportedDocumentMessage)
        }
        val recognitionTimeoutTitle: String = getStringFromJSONObject(jsonUISettings, "recognitionTimeoutTitle")
        if (recognitionTimeoutTitle != null) {
            overlasStringsBuilder.setRecognitionTimeoutTitle(recognitionTimeoutTitle)
        }
        val recognitionTimeoutMessage: String = getStringFromJSONObject(jsonUISettings, "recognitionTimeoutMessage")
        if (recognitionTimeoutMessage != null) {
            overlasStringsBuilder.setRecognitionTimeoutMessage(recognitionTimeoutMessage)
        }
        val retryButtonText: String = getStringFromJSONObject(jsonUISettings, "retryButtonText")
        if (retryButtonText != null) {
            overlasStringsBuilder.setRetryButtonText(retryButtonText)
        }
        val scanBarcodeText: String = getStringFromJSONObject(jsonUISettings, "scanBarcodeText")
        if (scanBarcodeText != null) {
            overlasStringsBuilder.setBackSideBarcodeInstructions(scanBarcodeText)
        }
        val errorDocumentTooCloseToEdge: String = getStringFromJSONObject(jsonUISettings, "errorDocumentTooCloseToEdge")
        if (errorDocumentTooCloseToEdge != null) {
            overlasStringsBuilder.setErrorDocumentTooCloseToEdge(errorDocumentTooCloseToEdge)
        }
        settings.setStrings(overlasStringsBuilder.build())
        return settings
    }

    @get:Override
    override val jsonName: String?
        get() = "BlinkIdOverlaySettings"
}