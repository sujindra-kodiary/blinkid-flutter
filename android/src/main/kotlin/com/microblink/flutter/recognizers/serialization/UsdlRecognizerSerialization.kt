package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.microblink.flutter.SerializationUtils
import kotlin.jvm.Throws

class UsdlRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonRecognizer: JSONObject): Recognizer<*> {
        val recognizer: com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer = UsdlRecognizer()
        recognizer.setNullQuietZoneAllowed(jsonRecognizer.optBoolean("nullQuietZoneAllowed", true))
        recognizer.setUncertainDecoding(jsonRecognizer.optBoolean("uncertainDecoding", true))
        return recognizer
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: UsdlRecognizer.Result = (recognizer as UsdlRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
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

    private fun serializeFields(result: UsdlRecognizer.Result): JSONArray {
        val fieldsArr = JSONArray()
        for (i in 0 until UsdlKeys.values().length) {
            fieldsArr.put(result.getField(UsdlKeys.values().get(i)))
        }
        return fieldsArr
    }

    @get:Override
    override val jsonName: String?
        get() = "UsdlRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer::class.java
}