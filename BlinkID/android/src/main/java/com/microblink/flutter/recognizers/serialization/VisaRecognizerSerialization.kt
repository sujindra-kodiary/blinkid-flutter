package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import com.microblink.flutter.SerializationUtils
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class VisaRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonObject: JSONObject): Recognizer<*> {
        val recognizer: com.microblink.entities.recognizers.blinkid.visa.VisaRecognizer = VisaRecognizer()
        recognizer.setDetectGlare(jsonObject.optBoolean("detectGlare", true))
        recognizer.setFaceImageDpi(jsonObject.optInt("faceImageDpi", 250))
        recognizer.setFullDocumentImageDpi(jsonObject.optInt("fullDocumentImageDpi", 250))
        recognizer.setFullDocumentImageExtensionFactors(SerializationUtils.deserializeExtensionFactors(jsonObject.optJSONObject("fullDocumentImageExtensionFactors")))
        recognizer.setReturnFaceImage(jsonObject.optBoolean("returnFaceImage", false))
        recognizer.setReturnFullDocumentImage(jsonObject.optBoolean("returnFullDocumentImage", false))
        return recognizer
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: com.microblink.entities.recognizers.blinkid.visa.VisaRecognizer.Result = (recognizer as com.microblink.entities.recognizers.blinkid.visa.VisaRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()))
            jsonResult.put("fullDocumentImage", SerializationUtils.encodeImageBase64(result.getFullDocumentImage()))
            jsonResult.put("mrzResult", BlinkIDSerializationUtils.serializeMrzResult(result.getMrzResult()))
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
        return jsonResult
    }

    @get:Override
    override val jsonName: String?
        get() = "VisaRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkid.visa.VisaRecognizer::class.java
}