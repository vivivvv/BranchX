package com.app.mybase.views.historyactivity

import androidx.lifecycle.MutableLiveData
import com.app.mybase.base.BaseViewModel
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : BaseViewModel() {

    var noAvailableHistoryText = MutableLiveData<Boolean>(false)
    var historyRVVisibility = MutableLiveData<Boolean>(false)

    fun showNoAvailableHistoryText() {
        noAvailableHistoryText.value = true
    }

    fun hideNoAvailableHistoryText() {
        noAvailableHistoryText.value = false
    }

    fun showHistoryRVVisibility() {
        historyRVVisibility.value = true
    }

    fun hideHistoryRVVisibility() {
        historyRVVisibility.value = false
    }

}