package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys
import com.microblink.entities.recognizers.blinkid.usdl.UsdlCombinedRecognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.microblink.flutter.SerializationUtils
import kotlin.jvm.Throws

class UsdlCombinedRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonRecognizer: JSONObject): Recognizer<*> {
        val recognizer = UsdlCombinedRecognizer()
        recognizer.setFaceImageDpi(jsonRecognizer.optInt("faceImageDpi", 250))
        recognizer.setFullDocumentImageDpi(jsonRecognizer.optInt("fullDocumentImageDpi", 250))
        recognizer.setFullDocumentImageExtensionFactors(SerializationUtils.deserializeExtensionFactors(jsonRecognizer.optJSONObject("fullDocumentImageExtensionFactors")))
        recognizer.setNumStableDetectionsThreshold(jsonRecognizer.optInt("numStableDetectionsThreshold", 6))
        recognizer.setReturnFaceImage(jsonRecognizer.optBoolean("returnFaceImage", false))
        recognizer.setReturnFullDocumentImage(jsonRecognizer.optBoolean("returnFullDocumentImage", false))
        recognizer.setSignResult(jsonRecognizer.optBoolean("signResult", false))
        return recognizer
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: UsdlCombinedRecognizer.Result = (recognizer as UsdlCombinedRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("digitalSignature", SerializationUtils.encodeByteArrayToBase64(result.getDigitalSignature()))
            jsonResult.put("digitalSignatureVersion", result.getDigitalSignatureVersion())
            jsonResult.put("documentDataMatch", SerializationUtils.serializeEnum(result.getDocumentDataMatch()))
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()))
            jsonResult.put("fullDocumentImage", SerializationUtils.encodeImageBase64(result.getFullDocumentImage()))
            jsonResult.put("scanningFirstSideDone", result.isScanningFirstSideDone())
            jsonResult.put("optionalElements", SerializationUtils.serializeStringArray(result.getOptionalElements()))
            jsonResult.put("rawData", SerializationUtils.encodeByteArrayToBase64(result.getRawData()))
            jsonResult.put("rawStringData", result.getRawStringData())
            jsonResult.put("uncertain", result.isUncertain())
            jsonResult.put("fields", serializeFields(result))
            jsonResult.put("firstName", result.getFirstName())
            jsonResult.put("lastName", result.getLastName())
            jsonResult.put("fullName", result.getFullName())
            jsonResult.put("address", result.getAddress())
            jsonResult.put("documentNumber", result.getDocumentNumber())
            jsonResult.put("sex", result.getSex())
            jsonResult.put("age", result.getAge())
            jsonResult.put("restrictions", result.getRestrictions())
            jsonResult.put("endorsements", result.getEndorsements())
            jsonResult.put("vehicleClass", result.getVehicleClass())
            jsonResult.put("dateOfBirth", SerializationUtils.serializeDate(result.getDateOfBirth()))
            jsonResult.put("dateOfIssue", SerializationUtils.serializeDate(result.getDateOfIssue()))
            jsonResult.put("dateOfExpiry", SerializationUtils.serializeDate(result.getDateOfExpiry()))
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
        return jsonResult
    }

    private fun serializeFields(result: UsdlCombinedRecognizer.Result): JSONArray {
        val fieldsArr = JSONArray()
        for (i in 0 until UsdlKeys.values().length) {
            fieldsArr.put(result.getField(UsdlKeys.values().get(i)))
        }
        return fieldsArr
    }

    @get:Override
    override val jsonName: String?
        get() = "UsdlCombinedRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkid.usdl.UsdlCombinedRecognizer::class.java
}