package com.app.mybase.views.detailedactivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.app.mybase.base.BaseViewModel
import com.app.mybase.helper.ApisResponse
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DetailedViewModel @Inject constructor(
    private val detailedRepository: DetailedRepository,
    var application: Application
) : BaseViewModel() {

    var youTubePlayer: YouTubePlayer? = null

    fun getDataFromLocalJson() = liveData(Dispatchers.IO) {
        emit(ApisResponse.Loading)
        emit(detailedRepository.getDataFromLocalJson(application))
        emit(ApisResponse.Complete)
    }

    fun setVideo(youtubePlayerView: YouTubePlayerView, videoId: String) {
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@DetailedViewModel.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo(videoId, 0F)
            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality)
                Log.d("TAG", "onPlaybackQualityChange: $playbackQuality")
            }
        })
    }
}