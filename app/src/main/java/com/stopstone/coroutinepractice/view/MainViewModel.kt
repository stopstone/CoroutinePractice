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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    private val _countdownValue = MutableStateFlow(COUNTDOWN_VALUE)
    val countdownValue: StateFlow<Int> = _countdownValue

    private var countdownJob: Job? = null

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            _items.value = repository.generateData()
        }
    }

    fun toggleItemState(item: Item) {
        val newList = _items.value.toMutableList()
        val index = newList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            newList[index] = item.copy(checked = !item.checked)
            _items.value = newList
        }
    }

    private fun removeItems(removeItems: List<Item>) {
        val newList = _items.value.toMutableList()
        newList.removeAll(removeItems)
        _items.value = newList
    }

    fun deleteCheckedItems() {
        val checkedItems = _items.value.filter { it.checked }
        removeItems(checkedItems)
    }

    fun getItemsSize() = _items.value.count { it.checked }

    fun startCountdownTimer() {
        val removeItems = _items.value.filter { it.checked }
        if (removeItems.isNotEmpty()) {
            countdownJob?.cancel()
            _countdownValue.value = COUNTDOWN_VALUE

            countdownJob = viewModelScope.launch {
                for (i in COUNTDOWN_VALUE downTo COUNTDOWN_END) {
                    _countdownValue.emit(i) // 카운트다운
                    delay(TIME_MILLIS)
                }
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