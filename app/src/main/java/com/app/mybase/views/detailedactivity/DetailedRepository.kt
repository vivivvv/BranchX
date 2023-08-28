package com.app.mybase.views.detailedactivity

import android.app.Application
import com.app.mybase.helper.ApisResponse
import com.app.mybase.helper.Utils
import com.app.mybase.model.Item
import com.app.mybase.network.ApiStories
import javax.inject.Inject

class DetailedRepository @Inject constructor(var apiStories: ApiStories) {

    fun getDataFromLocalJson(application: Application): ApisResponse<List<Item>> {
        return try {
            val list =
                Utils.getDataFromLocalJson(application.applicationContext, "detailed_page_content.json")
            ApisResponse.Success(list)
        } catch (e: Exception) {
            ApisResponse.Error(e.message.toString())
        }
    }
}