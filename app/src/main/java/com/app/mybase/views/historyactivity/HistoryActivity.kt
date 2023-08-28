package com.app.mybase.views.historyactivity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mybase.R
import com.app.mybase.VideoListAdapter
import com.app.mybase.base.BaseActivity
import com.app.mybase.databinding.ActivityHistoryBinding
import com.app.mybase.helper.AppConstants
import com.app.mybase.helper.SharedPreferenceHelper.Companion.HISTORY_ITEM
import com.app.mybase.helper.Utils
import com.app.mybase.model.Item
import com.app.mybase.views.detailedactivity.DetailedActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class HistoryActivity : BaseActivity(), VideoListAdapter.OnClickListener {

    lateinit var binding: ActivityHistoryBinding
    lateinit var viewmodel: HistoryViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: VideoListAdapter
    private var videoDataList = ArrayList<Item>()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        viewmodel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        binding.historyViewModel = viewmodel
        binding.lifecycleOwner = this@HistoryActivity

        // Initialize recycler view
        initializeDataToRV()

    }

    private fun initializeDataToRV() {
        recyclerView = binding.videoRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VideoListAdapter(this@HistoryActivity, this@HistoryActivity)
        recyclerView.adapter = adapter
        adapter.setOnClickListener(this)

        // Clear and Update history data
        videoDataList.clear()
        videoDataList.addAll(sharedPreferences.getArrayList(HISTORY_ITEM))
        adapter.updateVideoDataList(videoDataList)
        if (videoDataList.isEmpty()) {
            viewmodel.showNoAvailableHistoryText()
            viewmodel.hideHistoryRVVisibility()
        } else {
            viewmodel.hideNoAvailableHistoryText()
            viewmodel.showHistoryRVVisibility()
        }
    }

    override fun onClick(position: Int, item: Item) {
        Intent(this, DetailedActivity::class.java).apply {
            putExtra(AppConstants.ITEM, item)
            sharedPreferences.putArrayList(
                HISTORY_ITEM,
                Utils.updateHistoryData(position, sharedPreferences, videoDataList)
            )
            startActivity(this)
        }
    }

}