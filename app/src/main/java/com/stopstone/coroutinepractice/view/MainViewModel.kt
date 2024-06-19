package com.stopstone.coroutinepractice.view

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopstone.coroutinepractice.data.model.Item
import com.stopstone.coroutinepractice.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    private val _countdownValue = MutableLiveData<Int>()
    val countdownValue: LiveData<Int> = _countdownValue

    private var countdownJob: Job? = null

    init {
        fetchItems()
    }

    private fun fetchItems() {
        _items.value = repository.generateData()
    }

    fun toggleItemState(item: Item) {
        removeItems(listOf(item))
        _items.value = repository.toggleItem(item)
    }

    fun removeItems(removeItems: List<Item>) {
        _items.value = repository.removeItems(removeItems)
    }

    fun startCountdownTimer() {
        countdownJob?.cancel()
        _countdownValue.value = COUNTDOWN_VALUE

        countdownJob = viewModelScope.launch {
            for (i in COUNTDOWN_VALUE downTo COUNTDOWN_END) {
                _countdownValue.value = i // 카운트다운
                delay(TIME_MILLIS)
            }
        }
    }

    fun cancelCountdownTimer() {
        countdownJob?.cancel()
    }

    fun resetCountdownTimer() {
        countdownJob?.cancel()
        startCountdownTimer()
    }

    companion object {
        const val COUNTDOWN_VALUE = 5
        const val COUNTDOWN_END = 0
        const val TIME_MILLIS = 1000L
    }
}