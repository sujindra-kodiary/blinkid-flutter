package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import com.microblink.flutter.SerializationUtils
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class PassportRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonObject: JSONObject): Recognizer<*> {
        val recognizer: com.microblink.entities.recognizers.blinkid.passport.PassportRecognizer = PassportRecognizer()
        recognizer.setAnonymizeNetherlandsMrz(jsonObject.optBoolean("anonymizeNetherlandsMrz", true))
        recognizer.setDetectGlare(jsonObject.optBoolean("detectGlare", true))
        recognizer.setFaceImageDpi(jsonObject.optInt("faceImageDpi", 250))
        recognizer.setFullDocumentImageDpi(jsonObject.optInt("fullDocumentImageDpi", 250))
        recognizer.setFullDocumentImageExtensionFactors(SerializationUtils.deserializeExtensionFactors(jsonObject.optJSONObject("fullDocumentImageExtensionFactors")))
        recognizer.setReturnFaceImage(jsonObject.optBoolean("returnFaceImage", false))
        recognizer.setReturnFullDocumentImage(jsonObject.optBoolean("returnFullDocumentImage", false))
        recognizer.setSignResult(jsonObject.optBoolean("signResult", false))
        return recognizer
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: com.microblink.entities.recognizers.blinkid.passport.PassportRecognizer.Result = (recognizer as com.microblink.entities.recognizers.blinkid.passport.PassportRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("digitalSignature", SerializationUtils.encodeByteArrayToBase64(result.getDigitalSignature()))
            jsonResult.put("digitalSignatureVersion", result.getDigitalSignatureVersion() as Int)
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
        get() = "PassportRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkid.passport.PassportRecognizer::class.java
}