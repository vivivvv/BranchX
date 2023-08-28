package com.app.mybase.helper

import android.content.Context
import android.util.Log
import java.io.IOException

object JsonParserHelper {

    // Method for read JSON file
    fun loadJSONFromAsset(context: Context, fileName: String): String {
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            Log.d("TAG", "onCreate: ioException $ioException")
        }
        return jsonString
    }

}