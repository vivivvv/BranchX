package com.app.mybase.views.detailedactivity

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mybase.R
import com.app.mybase.VideoListAdapter
import com.app.mybase.base.BaseActivity
import com.app.mybase.databinding.ActivityDetailedBinding
import com.app.mybase.helper.ApisResponse
import com.app.mybase.helper.AppConstants.ITEM
import com.app.mybase.helper.Utils
import com.app.mybase.helper.Utils.parcelable
import com.app.mybase.model.Item
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.android.AndroidInjection
import javax.inject.Inject

class DetailedActivity : BaseActivity() {

    val TAG = this::class.java.name
    lateinit var binding: ActivityDetailedBinding
    lateinit var viewmodel: DetailedViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: VideoListAdapter
    var videoDataList = ArrayList<Item>()
    lateinit var youtubePlayerView: YouTubePlayerView
    var removeItemFromRV = ArrayList<Item>()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailed)
        viewmodel = ViewModelProvider(this, factory)[DetailedViewModel::class.java]
        binding.detailedViewModel = viewmodel
        binding.lifecycleOwner = this@DetailedActivity

        // Update video data from HomePage
        initialVideoData()
        // Initialize recycler view
        initializeDataToRV()
        // Get API data
        updateUIData()

    }

    private fun initialVideoData() {
        intent.parcelable<Item>(ITEM)?.let {
            binding.descriptionText.text = it.snippet.description.ifEmpty { "No Description" }
            binding.channelName.text = it.snippet.channelTitle
            binding.publishedAtText.text = Utils.getTimeAgo(it.snippet.publishedAt)
            youtubePlayerView = binding.youtubePlayerViewDes
            lifecycle.addObserver(youtubePlayerView)
            viewmodel.setVideo(youtubePlayerView, it.id.videoId)
            Utils.setServiceIcon(
                this,
                it.snippet.thumbnails?.medium?.url ?: "",
                binding.serviceImage
            )
        }
    }

    private fun initializeDataToRV() {
        recyclerView = binding.videoRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VideoListAdapter(this@DetailedActivity, this@DetailedActivity)
        recyclerView.adapter = adapter
    }

    private fun updateUIData() {
        viewmodel.getDataFromLocalJson().observe(this, Observer { apiResponse ->
            when (apiResponse) {
                is ApisResponse.Success -> {
                    videoDataList.clear()
                    videoDataList.addAll(apiResponse.response)
                    removeItemFromRV.addAll(apiResponse.response)
                    adapter.updateVideoDataList(videoDataList)
                }
                is ApisResponse.Error -> {
                    Log.d(TAG, "check token result: ${apiResponse.exception}")
                    showToast(apiResponse.exception)
                }
                is ApisResponse.Loading -> {
                    viewmodel.showProgress()
                }
                is ApisResponse.Complete -> {
                    viewmodel.hideProgress()
                }
                else -> {}
            }
        })
    }

}