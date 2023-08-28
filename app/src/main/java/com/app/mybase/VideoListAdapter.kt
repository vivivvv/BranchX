package com.app.mybase

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.app.mybase.databinding.VideoListItemBinding
import com.app.mybase.helper.Utils
import com.app.mybase.model.Item
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoListAdapter(
    private var context: Context,
    private var lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    var videoDataList = ArrayList<Item>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateVideoDataList(
        videoDataList: ArrayList<Item>
    ) {
        this.videoDataList.clear()
        this.videoDataList.addAll(videoDataList)
        notifyDataSetChanged()
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = DataBindingUtil.inflate<VideoListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.video_list_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // sets the value to the each itemView
        holder.descriptionText.text =
            videoDataList[position].snippet.description.ifEmpty { "No Description" }
        holder.channelName.text = videoDataList[position].snippet.channelTitle
        holder.publishedAtText.text = Utils.getTimeAgo(videoDataList[position].snippet.publishedAt)
        // Update video
        setVideo(holder.youtubePlayerView, videoDataList[position].id.videoId, holder.overlayView)
        // Update Image
        Utils.setServiceIcon(
            context, videoDataList[position].snippet.thumbnails?.medium?.url ?: "",
            holder.serviceImage
        )
        holder.overlayView.setOnClickListener {
            onClickListener?.onClick(position, videoDataList[position])
        }
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, videoDataList[position])
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return videoDataList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: VideoListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var descriptionText: TextView = itemView.descriptionText
        var channelName: TextView = itemView.channelName
        var publishedAtText: TextView = itemView.publishedAtText
        var serviceImage: ImageView = itemView.serviceImage
        var youtubePlayerView: YouTubePlayerView = itemView.youtubePlayerView
        var overlayView: View = itemView.overlayView
    }

    private var onClickListener: OnClickListener? = null

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, item: Item)
    }

    private fun setVideo(
        youtubePlayerView: YouTubePlayerView,
        videoId: String,
        overlayView: View
    ) {
        lifecycleOwner.lifecycle.addObserver(youtubePlayerView)

        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0F)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                when (state) {
                    // when the video is CUED, show the overlay.
                    PlayerConstants.PlayerState.VIDEO_CUED -> overlayView.visibility = View.VISIBLE
                    // remove the overlay for every other state, so that we don't intercept clicks and the
                    // user can interact with the player.
                    else -> overlayView.visibility = View.GONE
                }
            }
        })
    }

}