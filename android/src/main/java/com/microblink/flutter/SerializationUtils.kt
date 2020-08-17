package com.microblink.flutter
//import com.microblink.entities.parsers.Parser;
import android.graphics.Bitmap
import android.util.Base64
import com.microblink.entities.recognizers.Recognizer
import com.microblink.geometry.Point
import com.microblink.geometry.Quadrilateral
import com.microblink.image.Image
import com.microblink.results.date.Date
import com.microblink.results.date.DateResult
import com.microblink.entities.Entity
import com.microblink.entities.recognizers.blinkid.imageoptions.extension.ImageExtensionFactors
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.jvm.Throws

object SerializationUtils {
    private const val COMPRESSED_IMAGE_QUALITY = 90

    @Throws(JSONException::class)
    fun <T : Recognizer.Result?> addCommonRecognizerResultData(jsonObject: JSONObject, result: T) {
        jsonObject.put("resultState", serializeEnum(result.getResultState()))
    }

    /*public static <T extends Parser.Result> void addCommonParserResultData(JSONObject jsonObject, T result) throws JSONException {
        jsonObject.put("resultState", serializeEnum(result.getResultState()));
    }*/
    @Throws(JSONException::class)
    fun serializeDate(@Nullable date: Date?): JSONObject? {
        return if (date != null) {
            val jsonDate = JSONObject()
            jsonDate.put("day", date.getDay())
            jsonDate.put("month", date.getMonth())
            jsonDate.put("year", date.getYear())
            jsonDate
        } else {
            null
        }
    }

    @Throws(JSONException::class)
    fun serializeDate(@Nullable dateResult: DateResult?): JSONObject? {
        return if (dateResult == null) {
            null
        } else {
            serializeDate(dateResult.getDate())
        }
    }

    fun serializeEnum(e: Enum): Int {
        return e.ordinal()
    }

    fun serializeStringArray(strings: Array<String?>): JSONArray {
        val jsonStrings = JSONArray()
        for (str in strings) {
            jsonStrings.put(str)
        }
        return jsonStrings
    }

    fun encodeImageBase64(image: Image?): String? {
        if (image == null) {
            return null
        }
        val resultImgBmp: Bitmap = image.convertToBitmap() ?: return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        val success: Boolean = resultImgBmp.compress(Bitmap.CompressFormat.JPEG, COMPRESSED_IMAGE_QUALITY, byteArrayOutputStream)
        var resultImgBase64: String? = null
        if (success) {
            resultImgBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
        }
        try {
            byteArrayOutputStream.close()
        } catch (ignorable: IOException) {
        }
        return resultImgBase64
    }

    fun encodeByteArrayToBase64(arr: ByteArray?): String? {
        return if (arr == null) {
            null
        } else Base64.encodeToString(arr, Base64.NO_WRAP)
    }

    @Throws(JSONException::class)
    fun serializePoint(point: Point): JSONObject {
        val jsonPoint = JSONObject()
        jsonPoint.put("x", point.getX())
        jsonPoint.put("y", point.getY())
        return jsonPoint
    }

    @Throws(JSONException::class)
    fun serializeQuad(quad: Quadrilateral): JSONObject {
        val jsonQuad = JSONObject()
        jsonQuad.put("upperLeft", serializePoint(quad.getUpperLeft()))
        jsonQuad.put("upperRight", serializePoint(quad.getUpperRight()))
        jsonQuad.put("lowerLeft", serializePoint(quad.getLowerLeft()))
        jsonQuad.put("lowerRight", serializePoint(quad.getLowerRight()))
        return jsonQuad
    }

    fun getStringFromJSONObject(map: JSONObject, key: String?): String? {
        var value: String? = map.optString(key, null)
        if ("null".equals(value)) {
            value = null
        }
        return value
    }

    fun deserializeExtensionFactors(jsonExtensionFactors: JSONObject?): ImageExtensionFactors {
        return if (jsonExtensionFactors == null) {
            ImageExtensionFactors(0f, 0f, 0f, 0f)
        } else {
            val up = jsonExtensionFactors.optDouble("upFactor", 0.0) as Float
            val right = jsonExtensionFactors.optDouble("rightFactor", 0.0) as Float
            val down = jsonExtensionFactors.optDouble("downFactor", 0.0) as Float
            val left = jsonExtensionFactors.optDouble("leftFactor", 0.0) as Float
            ImageExtensionFactors(up, down, left, right)
        }
    }
}