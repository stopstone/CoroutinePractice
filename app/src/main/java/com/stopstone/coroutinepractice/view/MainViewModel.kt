package com.stopstone.coroutinepractice.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stopstone.coroutinepractice.data.model.Item
import com.stopstone.coroutinepractice.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    private val _trashItems = MutableLiveData<MutableList<Item>>()
    val trashItems: LiveData<MutableList<Item>> = _trashItems

    init {
        fetchItems();
    }

    private fun fetchItems() {
        _items.value = repository.generateData()
    }

    private fun removeItem(item: Item) {
        _items.value = repository.removeItem(item)
    }
    fun toggleItemState(item: Item) {
        removeItem(item)
        _trashItems.value = repository.toggleItem(item)
    }
}