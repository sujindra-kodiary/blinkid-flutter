package com.microblink.flutter.recognizers

import com.microblink.entities.recognizers.Recognizer
import org.json.JSONObject
import kotlin.jvm.Throws

interface RecognizerSerialization {
    fun createRecognizer(jsonObject: JSONObject?): Recognizer<*>?
    fun serializeResult(recognizer: Recognizer<*>?): JSONObject?
    val jsonName: String?
    val recognizerClass: Class<*>?
}