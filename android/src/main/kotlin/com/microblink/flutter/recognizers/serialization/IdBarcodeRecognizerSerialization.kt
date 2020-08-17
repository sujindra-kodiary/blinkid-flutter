package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import com.microblink.flutter.SerializationUtils
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class IdBarcodeRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonObject: JSONObject?): Recognizer<*> {
        return IdBarcodeRecognizer()
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: com.microblink.entities.recognizers.blinkid.idbarcode.IdBarcodeRecognizer.Result = (recognizer as com.microblink.entities.recognizers.blinkid.idbarcode.IdBarcodeRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("additionalNameInformation", result.getAdditionalNameInformation())
            jsonResult.put("address", result.getAddress())
            jsonResult.put("age", result.getAge())
            jsonResult.put("barcodeType", SerializationUtils.serializeEnum(result.getBarcodeType()))
            jsonResult.put("city", result.getCity())
            jsonResult.put("dateOfBirth", SerializationUtils.serializeDate(result.getDateOfBirth()))
            jsonResult.put("dateOfExpiry", SerializationUtils.serializeDate(result.getDateOfExpiry()))
            jsonResult.put("dateOfIssue", SerializationUtils.serializeDate(result.getDateOfIssue()))
            jsonResult.put("documentAdditionalNumber", result.getDocumentAdditionalNumber())
            jsonResult.put("documentNumber", result.getDocumentNumber())
            jsonResult.put("documentType", SerializationUtils.serializeEnum(result.getDocumentType()))
            jsonResult.put("employer", result.getEmployer())
            jsonResult.put("endorsements", result.getEndorsements())
            jsonResult.put("expired", result.isExpired())
            jsonResult.put("firstName", result.getFirstName())
            jsonResult.put("fullName", result.getFullName())
            jsonResult.put("issuingAuthority", result.getIssuingAuthority())
            jsonResult.put("jurisdiction", result.getJurisdiction())
            jsonResult.put("lastName", result.getLastName())
            jsonResult.put("maritalStatus", result.getMaritalStatus())
            jsonResult.put("nationality", result.getNationality())
            jsonResult.put("personalIdNumber", result.getPersonalIdNumber())
            jsonResult.put("placeOfBirth", result.getPlaceOfBirth())
            jsonResult.put("postalCode", result.getPostalCode())
            jsonResult.put("profession", result.getProfession())
            jsonResult.put("race", result.getRace())
            jsonResult.put("rawData", SerializationUtils.encodeByteArrayToBase64(result.getRawData()))
            jsonResult.put("religion", result.getReligion())
            jsonResult.put("residentialStatus", result.getResidentialStatus())
            jsonResult.put("restrictions", result.getRestrictions())
            jsonResult.put("sex", result.getSex())
            jsonResult.put("street", result.getStreet())
            jsonResult.put("stringData", result.getStringData())
            jsonResult.put("uncertain", result.isUncertain())
            jsonResult.put("vehicleClass", result.getVehicleClass())
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
        return jsonResult
    }

    @get:Override
    override val jsonName: String?
        get() = "IdBarcodeRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkid.idbarcode.IdBarcodeRecognizer::class.java
}