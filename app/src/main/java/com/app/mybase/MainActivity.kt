package com.app.mybase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mybase.base.BaseActivity
import com.app.mybase.databinding.ActivityMainBinding
import com.app.mybase.helper.ApisResponse
import com.app.mybase.helper.AppConstants.ITEM
import com.app.mybase.helper.SharedPreferenceHelper.Companion.HISTORY_ITEM
import com.app.mybase.helper.Utils
import com.app.mybase.model.Item
import com.app.mybase.views.detailedactivity.DetailedActivity
import com.app.mybase.views.historyactivity.HistoryActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity(), VideoListAdapter.OnClickListener {

    val TAG = this::class.java.name
    lateinit var binding: ActivityMainBinding
    lateinit var viewmodel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: VideoListAdapter
    var videoDataList = ArrayList<Item>()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewmodel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.mainViewModel = viewmodel
        binding.lifecycleOwner = this@MainActivity

        // Initialize recycler view
        initializeDataToRV()
        // Listeners
        clickListeners()
        // Get API data
        updateUIData()

    }

    private fun initializeDataToRV() {
        recyclerView = binding.videoRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VideoListAdapter(this@MainActivity, this@MainActivity)
        recyclerView.adapter = adapter
        adapter.setOnClickListener(this)
    }

    private fun updateUIData() {
        viewmodel.getDataFromLocalJson().observe(this, Observer { apiResponse ->
            when (apiResponse) {
                is ApisResponse.Success -> {
                    videoDataList.clear()
                    videoDataList.addAll(apiResponse.response)
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

    private fun clickListeners() {
        binding.historyBtn.setOnClickListener {
            Intent(this, HistoryActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onClick(position: Int, item: Item) {
        Intent(this, DetailedActivity::class.java).apply {
            putExtra(ITEM, videoDataList[position])
            // Update History List
            sharedPreferences.putArrayList(
                HISTORY_ITEM,
                Utils.updateHistoryData(position, sharedPreferences, videoDataList)
            )
            startActivity(this)
        }
    }

}