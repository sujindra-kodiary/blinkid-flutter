package com.microblink.flutter.recognizers

import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.flutter.recognizers.serialization.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import kotlin.jvm.Throws

enum class RecognizerSerializers {
    INSTANCE;

    private val mByJSONName: HashMap<String, RecognizerSerialization> = HashMap()
    private val mByClass: HashMap<Class<*>, RecognizerSerialization> = HashMap()
    private fun registerMapping(recognizerSerialization: RecognizerSerialization) {
        mByJSONName.put(recognizerSerialization.getJsonName(), recognizerSerialization)
        mByClass.put(recognizerSerialization.getRecognizerClass(), recognizerSerialization)
    }

    @Throws(JSONException::class)
    fun getRecognizerSerialization(jsonRecognizer: JSONObject): RecognizerSerialization {
        return mByJSONName.get(jsonRecognizer.getString("recognizerType"))
    }

    fun getRecognizerSerialization(recognizer: Recognizer<*>): RecognizerSerialization {
        return mByClass.get(recognizer.getClass())
    }

    fun deserializeRecognizerCollection(jsonRecognizerCollection: JSONObject): RecognizerBundle {
        return try {
            val recognizerArray: JSONArray = jsonRecognizerCollection.getJSONArray("recognizerArray")
            val numRecognizers: Int = recognizerArray.length()
            val recognizers: Array<Recognizer<*>?> = arrayOfNulls<Recognizer>(numRecognizers)
            for (i in 0 until numRecognizers) {
                recognizers[i] = getRecognizerSerialization(recognizerArray.getJSONObject(i)).createRecognizer(recognizerArray.getJSONObject(i))
            }
            val recognizerBundle = RecognizerBundle(recognizers)
            recognizerBundle.setAllowMultipleScanResultsOnSingleImage(jsonRecognizerCollection.optBoolean("allowMultipleResults", false))
            recognizerBundle.setNumMsBeforeTimeout(jsonRecognizerCollection.optInt("milisecondsBeforeTimeout", 10000))
            recognizerBundle
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }

    fun serializeRecognizerResults(recognizers: Array<Recognizer<*>?>): JSONArray {
        val jsonArray = JSONArray()
        for (recognizer in recognizers) {
            jsonArray.put(getRecognizerSerialization(recognizer).serializeResult(recognizer))
        }
        return jsonArray
    }

    init {
        registerMapping(SuccessFrameGrabberRecognizerSerialization())
        registerMapping(BlinkIdCombinedRecognizerSerialization())
        registerMapping(BlinkIdRecognizerSerialization())
        registerMapping(DocumentFaceRecognizerSerialization())
        registerMapping(IdBarcodeRecognizerSerialization())
        registerMapping(MrtdCombinedRecognizerSerialization())
        registerMapping(MrtdRecognizerSerialization())
        registerMapping(PassportRecognizerSerialization())
        registerMapping(VisaRecognizerSerialization())
        registerMapping(UsdlRecognizerSerialization())
        registerMapping(UsdlCombinedRecognizerSerialization())
    }
}