package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import com.microblink.flutter.SerializationUtils
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class DocumentFaceRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonObject: JSONObject): Recognizer<*> {
        val recognizer: com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer = DocumentFaceRecognizer()
        recognizer.setDetectorType(com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceDetectorType.values().get(jsonObject.optInt("detectorType", 1) - 1))
        recognizer.setFaceImageDpi(jsonObject.optInt("faceImageDpi", 250))
        recognizer.setFullDocumentImageDpi(jsonObject.optInt("fullDocumentImageDpi", 250))
        recognizer.setFullDocumentImageExtensionFactors(SerializationUtils.deserializeExtensionFactors(jsonObject.optJSONObject("fullDocumentImageExtensionFactors")))
        recognizer.setNumStableDetectionsThreshold(jsonObject.optInt("numStableDetectionsThreshold", 6))
        recognizer.setReturnFaceImage(jsonObject.optBoolean("returnFaceImage", false))
        recognizer.setReturnFullDocumentImage(jsonObject.optBoolean("returnFullDocumentImage", false))
        return recognizer
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer.Result = (recognizer as com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("documentLocation", SerializationUtils.serializeQuad(result.getDocumentLocation()))
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()))
            jsonResult.put("faceLocation", SerializationUtils.serializeQuad(result.getFaceLocation()))
            jsonResult.put("fullDocumentImage", SerializationUtils.encodeImageBase64(result.getFullDocumentImage()))
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
        return jsonResult
    }

    @get:Override
    override val jsonName: String?
        get() = "DocumentFaceRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer::class.java
}