package com.stopstone.coroutinepractice.view

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
    init {
        fetchItems()
    }

    private var countdownJob: Job? = null

    private fun fetchItems() {
        _items.value = repository.generateData()
    }

    private fun removeItem(item: Item) {
        _items.value = repository.removeItem(item)
    }

    fun toggleItemState(item: Item) {
        removeItem(item)
        _items.value = repository.toggleItem(item)
    }

    fun startCountdownTimer() {
        countdownJob?.cancel()
        _countdownValue.value = 5

        countdownJob = viewModelScope.launch {
            for (i in 5 downTo 0) {
                delay(1000)
                _countdownValue.value = i // 카운트다운
            }
            _countdownValue.value = 0 // 끝나면 0
        }
    }

    fun cancelCountdownTimer() {
        countdownJob?.cancel()
    }

    fun removeCheckedItems(checkedItems: List<Item>) {
        _items.value = repository.removeCheckedItems(checkedItems)
    }
}