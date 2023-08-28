package com.app.mybase.helper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.app.mybase.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

// Helps to store history data
@Singleton
class SharedPreferenceHelper @Inject constructor(application: Application) {

    companion object {
        const val HISTORY_ITEM = "history_item"
    }

    private var sharedPreference: SharedPreferences =
        application.applicationContext.getSharedPreferences("MyUser", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor? = sharedPreference.edit()

    fun putArrayList(key: String, obj: Any) {
        Gson().apply {
            val json = this.toJson(obj)
            editor?.putString(key, json)
            editor?.apply()
        }
    }

    fun getArrayList(key: String): ArrayList<Item> {
        return Gson().let {
            val json = sharedPreference.getString(key, "")
            val type: Type = object : TypeToken<ArrayList<Item>>() {}.type
            it.fromJson(json, type)
        }
    }

}