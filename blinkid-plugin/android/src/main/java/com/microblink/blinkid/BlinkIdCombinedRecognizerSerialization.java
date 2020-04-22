package com.microblink.blinkid;

import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

public final class BlinkIdCombinedRecognizerSerialization {

    public static JSONObject serializeResult(Recognizer<?> recognizer) {
        BlinkIdCombinedRecognizer.Result result = ((BlinkIdCombinedRecognizer)recognizer).getResult();
        JSONObject jsonResult = new JSONObject();
        try {
            SerializationUtils.addCommonResultData(jsonResult, result);
            jsonResult.put("additionalAddressInformation", result.getAdditionalAddressInformation());
            jsonResult.put("additionalNameInformation", result.getAdditionalNameInformation());
            jsonResult.put("address", result.getAddress());
            jsonResult.put("conditions", result.getConditions());
            jsonResult.put("dateOfBirth", SerializationUtils.serializeDate(result.getDateOfBirth()));
            jsonResult.put("dateOfExpiry", SerializationUtils.serializeDate(result.getDateOfExpiry()));
            jsonResult.put("dateOfExpiryPermanent", result.isDateOfExpiryPermanent());
            jsonResult.put("dateOfIssue", SerializationUtils.serializeDate(result.getDateOfIssue()));
            jsonResult.put("digitalSignature", SerializationUtils.encodeByteArrayToBase64(result.getDigitalSignature()));
            jsonResult.put("digitalSignatureVersion", (int)result.getDigitalSignatureVersion());
            jsonResult.put("documentAdditionalNumber", result.getDocumentAdditionalNumber());
            jsonResult.put("documentDataMatch", SerializationUtils.serializeEnum(result.getDocumentDataMatch()));
            jsonResult.put("documentNumber", result.getDocumentNumber());
            jsonResult.put("driverLicenseDetailedInfo", SerializationUtils.serializeDriverLicenseDetailedInfo(result.getDriverLicenseDetailedInfo()));
            jsonResult.put("employer", result.getEmployer());
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()));
            jsonResult.put("firstName", result.getFirstName());
            jsonResult.put("fullDocumentBackImage", SerializationUtils.encodeImageBase64(result.getFullDocumentBackImage()));
            jsonResult.put("fullDocumentFrontImage", SerializationUtils.encodeImageBase64(result.getFullDocumentFrontImage()));
            jsonResult.put("fullName", result.getFullName());
            jsonResult.put("issuingAuthority", result.getIssuingAuthority());
            jsonResult.put("lastName", result.getLastName());
            jsonResult.put("localizedName", result.getLocalizedName());
            jsonResult.put("maritalStatus", result.getMaritalStatus());
            jsonResult.put("mrzResult", SerializationUtils.serializeMrzResult(result.getMrzResult()));
            jsonResult.put("nationality", result.getNationality());
            jsonResult.put("personalIdNumber", result.getPersonalIdNumber());
            jsonResult.put("placeOfBirth", result.getPlaceOfBirth());
            jsonResult.put("profession", result.getProfession());
            jsonResult.put("race", result.getRace());
            jsonResult.put("religion", result.getReligion());
            jsonResult.put("residentialStatus", result.getResidentialStatus());
            jsonResult.put("scanningFirstSideDone", result.isScanningFirstSideDone());
            jsonResult.put("sex", result.getSex());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonResult;
    }
}
