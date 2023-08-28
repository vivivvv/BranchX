package com.app.mybase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeResponse(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
) : Parcelable

@Parcelize
data class Thumbnails(
    val default: Default,
    val high: High,
    val medium: Medium
) : Parcelable

@Parcelize
data class Snippet(
    val channelId: String,
    val channelTitle: String,
    val description: String,
    val liveBroadcastContent: String,
    val publishTime: String,
    val publishedAt: String,
    val thumbnails: Thumbnails?,
    val title: String
) : Parcelable

@Parcelize
data class PageInfo(
    val resultsPerPage: Int,
    val totalResults: Int
) : Parcelable

@Parcelize
data class Medium(
    val height: Int,
    val url: String,
    val width: Int
) : Parcelable

@Parcelize
data class Item(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet
) : Parcelable

@Parcelize
data class Id(
    val channelId: String = "",
    val kind: String = "",
    val playlistId: String = "",
    val videoId: String = ""
) : Parcelable

@Parcelize
data class Default(
    val height: Int,
    val url: String,
    val width: Int
) : Parcelable

@Parcelize
data class High(
    val height: Int,
    val url: String,
    val width: Int
) : Parcelable