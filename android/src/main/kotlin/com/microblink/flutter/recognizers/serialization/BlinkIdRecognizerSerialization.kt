package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import com.microblink.flutter.SerializationUtils
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class BlinkIdRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonObject: JSONObject): Recognizer<*> {
        val recognizer: com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer = BlinkIdRecognizer()
        recognizer.setAllowBlurFilter(jsonObject.optBoolean("allowBlurFilter", true))
        recognizer.setAllowUnparsedMrzResults(jsonObject.optBoolean("allowUnparsedMrzResults", false))
        recognizer.setAllowUnverifiedMrzResults(jsonObject.optBoolean("allowUnverifiedMrzResults", true))
        recognizer.setAnonymizationMode(com.microblink.entities.recognizers.blinkid.generic.AnonymizationMode.values().get(jsonObject.optInt("anonymizationMode", 4) - 1))
        recognizer.setFaceImageDpi(jsonObject.optInt("faceImageDpi", 250))
        recognizer.setFullDocumentImageDpi(jsonObject.optInt("fullDocumentImageDpi", 250))
        recognizer.setFullDocumentImageExtensionFactors(SerializationUtils.deserializeExtensionFactors(jsonObject.optJSONObject("fullDocumentImageExtensionFactors")))
        recognizer.setPaddingEdge(jsonObject.optDouble("paddingEdge", 0.0) as Float)
        recognizer.setReturnFaceImage(jsonObject.optBoolean("returnFaceImage", false))
        recognizer.setReturnFullDocumentImage(jsonObject.optBoolean("returnFullDocumentImage", false))
        recognizer.setValidateResultCharacters(jsonObject.optBoolean("validateResultCharacters", true))
        return recognizer
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val result: com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer.Result = (recognizer as com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer).getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("additionalAddressInformation", result.getAdditionalAddressInformation())
            jsonResult.put("additionalNameInformation", result.getAdditionalNameInformation())
            jsonResult.put("address", result.getAddress())
            jsonResult.put("age", result.getAge())
            jsonResult.put("barcodeResult", BlinkIDSerializationUtils.serializeBarcodeResult(result.getBarcodeResult()))
            jsonResult.put("classInfo", BlinkIDSerializationUtils.serializeClassInfo(result.getClassInfo()))
            jsonResult.put("conditions", result.getConditions())
            jsonResult.put("dateOfBirth", SerializationUtils.serializeDate(result.getDateOfBirth()))
            jsonResult.put("dateOfExpiry", SerializationUtils.serializeDate(result.getDateOfExpiry()))
            jsonResult.put("dateOfExpiryPermanent", result.isDateOfExpiryPermanent())
            jsonResult.put("dateOfIssue", SerializationUtils.serializeDate(result.getDateOfIssue()))
            jsonResult.put("documentAdditionalNumber", result.getDocumentAdditionalNumber())
            jsonResult.put("documentNumber", result.getDocumentNumber())
            jsonResult.put("driverLicenseDetailedInfo", BlinkIDSerializationUtils.serializeDriverLicenseDetailedInfo(result.getDriverLicenseDetailedInfo()))
            jsonResult.put("employer", result.getEmployer())
            jsonResult.put("expired", result.isExpired())
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()))
            jsonResult.put("firstName", result.getFirstName())
            jsonResult.put("fullDocumentImage", SerializationUtils.encodeImageBase64(result.getFullDocumentImage()))
            jsonResult.put("fullName", result.getFullName())
            jsonResult.put("imageAnalysisResult", BlinkIDSerializationUtils.serializeImageAnalysisResult(result.getImageAnalysisResult()))
            jsonResult.put("issuingAuthority", result.getIssuingAuthority())
            jsonResult.put("lastName", result.getLastName())
            jsonResult.put("localizedName", result.getLocalizedName())
            jsonResult.put("maritalStatus", result.getMaritalStatus())
            jsonResult.put("mrzResult", BlinkIDSerializationUtils.serializeMrzResult(result.getMrzResult()))
            jsonResult.put("nationality", result.getNationality())
            jsonResult.put("personalIdNumber", result.getPersonalIdNumber())
            jsonResult.put("placeOfBirth", result.getPlaceOfBirth())
            jsonResult.put("profession", result.getProfession())
            jsonResult.put("race", result.getRace())
            jsonResult.put("religion", result.getReligion())
            jsonResult.put("residentialStatus", result.getResidentialStatus())
            jsonResult.put("sex", result.getSex())
            jsonResult.put("vizResult", BlinkIDSerializationUtils.serializeVizResult(result.getVizResult()))
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
        return jsonResult
    }

    @get:Override
    override val jsonName: String?
        get() = "BlinkIdRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer::class.java
}