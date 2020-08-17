package com.microblink.flutter.recognizers.serialization

import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.successframe.SuccessFrameGrabberRecognizer
import com.microblink.flutter.recognizers.RecognizerSerialization
import com.microblink.flutter.recognizers.RecognizerSerializers
import com.microblink.flutter.SerializationUtils
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class SuccessFrameGrabberRecognizerSerialization : RecognizerSerialization {
    @Override
    override fun createRecognizer(jsonRecognizer: JSONObject): Recognizer<*> {
        return try {
            // first obtain slave recognizer
            val jsonSlaveRecognizer: JSONObject = jsonRecognizer.getJSONObject("slaveRecognizer")
            val slaveRecognizer: Recognizer<*> = RecognizerSerializers.INSTANCE.getRecognizerSerialization(jsonSlaveRecognizer).createRecognizer(jsonSlaveRecognizer)
            SuccessFrameGrabberRecognizer(slaveRecognizer)
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
    }

    @Override
    override fun serializeResult(recognizer: Recognizer<*>): JSONObject {
        val sfgr: SuccessFrameGrabberRecognizer = recognizer as SuccessFrameGrabberRecognizer
        val jsonSlaveResult: JSONObject = RecognizerSerializers.INSTANCE.getRecognizerSerialization(sfgr.getSlaveRecognizer()).serializeResult(sfgr.getSlaveRecognizer())
        val result: SuccessFrameGrabberRecognizer.Result = sfgr.getResult()
        val jsonResult = JSONObject()
        try {
            SerializationUtils.addCommonRecognizerResultData(jsonResult, result)
            jsonResult.put("slaveRecognizerResult", jsonSlaveResult)
            jsonResult.put("successFrame", SerializationUtils.encodeImageBase64(result.getSuccessFrame()))
        } catch (e: JSONException) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw RuntimeException(e)
        }
        return jsonResult
    }

    @get:Override
    override val jsonName: String?
        get() = "SuccessFrameGrabberRecognizer"

    @get:Override
    override val recognizerClass: Class<*>
        get() = SuccessFrameGrabberRecognizer::class.java
}