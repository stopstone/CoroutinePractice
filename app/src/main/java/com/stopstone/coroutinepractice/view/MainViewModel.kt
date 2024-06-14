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
    private val _items = MutableLiveData<List<Item>>();
    val items: LiveData<List<Item>> = _items;

    init {
        fetchItems();
    }

    private fun fetchItems() {
        _items.value = repository.generateData()
    }

}