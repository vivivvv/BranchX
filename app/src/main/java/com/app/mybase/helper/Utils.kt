package com.app.mybase.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.widget.ImageView
import com.app.mybase.R
import com.app.mybase.model.*
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// Class help to connect common functions
object Utils {

    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setServiceIcon(
        context: Context,
        serviceTemplateIcon: String?,
        serviceIconView: ImageView
    ) {
        if (serviceTemplateIcon != null) {
            Glide.with(context)
                .load(serviceTemplateIcon)
                .error(R.color.white)
                .into(serviceIconView)
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.with(context).clear(serviceIconView)
            serviceIconView.setImageDrawable(context.getDrawable(R.color.white))
        }
    }

    fun getDataFromLocalJson(context: Context, fileName: String): List<Item> {
        var itemList = ArrayList<Item>()
        val obj = JSONObject(
            JsonParserHelper.loadJSONFromAsset(context, fileName)
        )
        val jsonArray = obj.getJSONArray("items")
        val kind = obj.getString("kind")
        val etag = obj.getString("etag")
        val nextPageToken = obj.getString("nextPageToken")
        val regionCode = obj.getString("regionCode")
        val pageInfo = obj.getJSONObject("pageInfo")

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray[i] as JSONObject
            val kindIn = obj.getString("kind")
            val etagIn = obj.getString("etag")
            val id = jsonObject.getJSONObject("id")
            val kindId = obj.getString("kind")
            val videoId = id.optString("videoId", "")
            val snippet = jsonObject.getJSONObject("snippet")
            val publishedAt = snippet.getString("publishedAt")
            val channelId = snippet.getString("channelId")
            val title = snippet.getString("title")
            val description = snippet.getString("description")
            val channelTitle = snippet.getString("channelTitle")
            val liveBroadcastContent = snippet.getString("liveBroadcastContent")
            val publishTime = snippet.getString("publishTime")

            if (videoId.isNotEmpty() && videoId.isNotBlank()) {
                val thumbnails = snippet.optJSONObject("thumbnails")?.let {
                    val default = Default(
                        it.optJSONObject("default")?.getInt("height") ?: 0,
                        it.optJSONObject("default")?.getString("url") ?: "",
                        it.optJSONObject("default")?.getInt("width") ?: 0
                    )
                    val medium = Medium(
                        it.optJSONObject("medium")?.getInt("height") ?: 0,
                        it.optJSONObject("medium")?.getString("url") ?: "",
                        it.optJSONObject("medium")?.getInt("width") ?: 0
                    )
                    val high = High(
                        it.optJSONObject("high")?.getInt("height") ?: 0,
                        it.optJSONObject("high")?.getString("url") ?: "",
                        it.optJSONObject("high")?.getInt("width") ?: 0
                    )
                    Thumbnails(default, high, medium)
                }
                itemList.add(
                    Item(
                        etagIn, Id(videoId = videoId, kind = kindId), kindIn,
                        Snippet(
                            channelId,
                            channelTitle,
                            description,
                            liveBroadcastContent,
                            publishTime,
                            publishedAt,
                            thumbnails,
                            title
                        )
                    )
                )
            }
        }
        // Sorting list based on publish time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        itemList =
            ArrayList<Item>(itemList.sortedBy { dateFormat.parse(it.snippet.publishTime) })
        return itemList.reversed()
    }

    fun getTimeAgo(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = sdf.parse(timestamp)
        return TimeAgo.using(date?.time ?: 0)
    }

    // For update the history data only 10 number of videos
    fun updateHistoryData(
        position: Int,
        sharedPreferenceHelper: SharedPreferenceHelper,
        videoDataList: ArrayList<Item>
    ): ArrayList<Item> {
        val updatedList = ArrayList<Item>()
        val historyList = sharedPreferenceHelper.getArrayList(SharedPreferenceHelper.HISTORY_ITEM)

        // Verify history list
        if (historyList.contains(videoDataList[position])) {
            historyList.remove(videoDataList[position])
            updatedList.add(0, videoDataList[position])
            updatedList.addAll(historyList)
        } else if (historyList.size >= 10) {
            historyList.removeAt(historyList.size - 1)
            updatedList.add(0, videoDataList[position])
            updatedList.addAll(historyList)
        } else {
            updatedList.add(0, videoDataList[position])
            updatedList.addAll(historyList)
        }
        // Update history list
        sharedPreferenceHelper.putArrayList(SharedPreferenceHelper.HISTORY_ITEM, updatedList)

        return updatedList
    }

}
