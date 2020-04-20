package com.microblink.blinkid;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.blinkid.generic.DriverLicenseDetailedInfo;
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult;
import com.microblink.image.Image;
import com.microblink.results.date.Date;
import com.microblink.results.date.DateResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializationUtils {

    private static final int COMPRESSED_IMAGE_QUALITY = 90;

    public static <T extends Recognizer.Result> void addCommonResultData(JSONObject jsonObject, T result) throws JSONException {
        jsonObject.put("resultState", serializeEnum(result.getResultState()));
    }

    public static JSONObject serializeDate( @Nullable  Date date ) throws JSONException {
        if (date != null ) {
            JSONObject jsonDate = new JSONObject();
            jsonDate.put("day", date.getDay());
            jsonDate.put("month", date.getMonth());
            jsonDate.put("year", date.getYear());
            return jsonDate;
        } else {
            return null;
        }
    }

    public static JSONObject serializeDate(@Nullable DateResult dateResult) throws JSONException {
        if (dateResult == null) {
            return null;
        } else {
            return serializeDate(dateResult.getDate());
        }
    }

    public static int serializeEnum(Enum e) {
        return e.ordinal() + 1;
    }

    public static String encodeImageBase64(Image image) {
        if (image == null) {
            return null;
        }
        Bitmap resultImgBmp = image.convertToBitmap();
        if (resultImgBmp == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean success = resultImgBmp.compress(Bitmap.CompressFormat.JPEG, COMPRESSED_IMAGE_QUALITY, byteArrayOutputStream);
        String resultImgBase64 = null;
        if (success) {
            resultImgBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException ignorable) {}
        return resultImgBase64;
    }

    public static String encodeByteArrayToBase64(byte[] arr) {
        if (arr == null) {
            return null;
        }
        return Base64.encodeToString(arr, Base64.NO_WRAP);
    }

    public static JSONObject serializeMrzResult(MrzResult mrzResult) throws JSONException {
        JSONObject jsonMrz = new JSONObject();
        jsonMrz.put("documentType", mrzResult.getDocumentType().ordinal() + 1);
        jsonMrz.put("primaryId", mrzResult.getPrimaryId());
        jsonMrz.put("secondaryId", mrzResult.getSecondaryId());
        jsonMrz.put("issuer", mrzResult.getIssuer());
        jsonMrz.put("dateOfBirth", SerializationUtils.serializeDate(mrzResult.getDateOfBirth().getDate()));
        jsonMrz.put("documentNumber", mrzResult.getDocumentNumber());
        jsonMrz.put("nationality", mrzResult.getNationality());
        jsonMrz.put("gender", mrzResult.getGender());
        jsonMrz.put("documentCode", mrzResult.getDocumentCode());
        jsonMrz.put("dateOfExpiry", SerializationUtils.serializeDate(mrzResult.getDateOfExpiry().getDate()));
        jsonMrz.put("opt1", mrzResult.getOpt1());
        jsonMrz.put("opt2", mrzResult.getOpt2());
        jsonMrz.put("alienNumber", mrzResult.getAlienNumber());
        jsonMrz.put("applicationReceiptNumber", mrzResult.getApplicationReceiptNumber());
        jsonMrz.put("immigrantCaseNumber", mrzResult.getImmigrantCaseNumber());
        jsonMrz.put("mrzText", mrzResult.getMrzText());
        jsonMrz.put("sanitizedOpt1", mrzResult.getSanitizedOpt1());
        jsonMrz.put("sanitizedOpt2", mrzResult.getSanitizedOpt2());
        jsonMrz.put("sanitizedNationality", mrzResult.getSanitizedNationality());
        jsonMrz.put("sanitizedIssuer", mrzResult.getSanitizedIssuer());
        jsonMrz.put("sanitizedDocumentCode", mrzResult.getSanitizedDocumentCode());
        jsonMrz.put("sanitizedDocumentNumber", mrzResult.getSanitizedDocumentNumber());
        jsonMrz.put("mrzParsed", mrzResult.isMrzParsed());
        jsonMrz.put("mrzVerified", mrzResult.isMrzVerified());
        return jsonMrz;
    }

    public static JSONObject serializeDriverLicenseDetailedInfo(DriverLicenseDetailedInfo dlDetailedInfo) throws JSONException {
        JSONObject jsonDriverLicenseDetailedInfo = new JSONObject();
        jsonDriverLicenseDetailedInfo.put("restrictions", dlDetailedInfo.getRestrictions());
        jsonDriverLicenseDetailedInfo.put("endorsements", dlDetailedInfo.getEndorsements());
        jsonDriverLicenseDetailedInfo.put("vehicleClass", dlDetailedInfo.getVehicleClass());
        return jsonDriverLicenseDetailedInfo;
    }

}
