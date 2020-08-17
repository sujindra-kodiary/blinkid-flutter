package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import com.microblink.flutter.SerializationUtils
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class MrtdCombinedRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonObject: JSONObject): Recognizer<*> {
        val recognizer: com.microblink.entities.recognizers.blinkid.mrtd.MrtdCombinedRecognizer = MrtdCombinedRecognizer()
        recognizer.setAllowSpecialCharacters(jsonObject.optBoolean("allowSpecialCharacters", false))
        recognizer.setAllowUnparsedResults(jsonObject.optBoolean("allowUnparsedResults", false))
        recognizer.setAllowUnverifiedResults(jsonObject.optBoolean("allowUnverifiedResults", false))
        recognizer.setDetectorType(com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceDetectorType.values().get(jsonObject.optInt("detectorType", 1) - 1))
        recognizer.setFaceImageDpi(jsonObject.optInt("faceImageDpi", 250))
        recognizer.setFullDocumentImageDpi(jsonObject.optInt("fullDocumentImageDpi", 250))
        recognizer.setFullDocumentImageExtensionFactors(SerializationUtils.deserializeExtensionFactors(jsonObject.optJSONObject("fullDocumentImageExtensionFactors")))
        recognizer.setNumStableDetectionsThreshold(jsonObject.optInt("numStableDetectionsThreshold", 6))
        recognizer.setReturnFaceImage(jsonObject.optBoolean("returnFaceImage", false))
        recognizer.setReturnFullDocumentImage(jsonObject.optBoolean("returnFullDocumentImage", false))
        recognizer.setSignResult(jsonObject.optBoolean("signResult", false))
        return recognizer
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: com.microblink.entities.recognizers.blinkid.mrtd.MrtdCombinedRecognizer.Result = (recognizer as com.microblink.entities.recognizers.blinkid.mrtd.MrtdCombinedRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("digitalSignature", SerializationUtils.encodeByteArrayToBase64(result.getDigitalSignature()))
            jsonResult.put("digitalSignatureVersion", result.getDigitalSignatureVersion() as Int)
            jsonResult.put("documentDataMatch", SerializationUtils.serializeEnum(result.getDocumentDataMatch()))
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()))
            jsonResult.put("fullDocumentBackImage", SerializationUtils.encodeImageBase64(result.getFullDocumentBackImage()))
            jsonResult.put("fullDocumentFrontImage", SerializationUtils.encodeImageBase64(result.getFullDocumentFrontImage()))
            jsonResult.put("mrzResult", BlinkIDSerializationUtils.serializeMrzResult(result.getMrzResult()))
            jsonResult.put("scanningFirstSideDone", result.isScanningFirstSideDone())
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
        return jsonResult
    }

    @get:Override
    override val jsonName: String?
        get() = "MrtdCombinedRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkid.mrtd.MrtdCombinedRecognizer::class.java
}