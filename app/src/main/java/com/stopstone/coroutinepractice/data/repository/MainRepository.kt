package com.stopstone.coroutinepractice.data.repository

import com.stopstone.coroutinepractice.data.model.Item
import javax.inject.Inject

class MainRepository @Inject constructor() {
    private val items = mutableListOf<Item>()

    fun generateData(): List<Item> {
        items.apply {
            repeat(SIZE) {
                val shuffledAlphabet = ('A'..'Z').shuffled()
                add(Item(it + 1, shuffledAlphabet.first()))
            }
        }
        return items
    }

    fun toggleItem(item: Item): List<Item> {
        items.add(item.copy(checked = !item.checked))
        return items
    }

    fun removeItems(checkedItems: List<Item>): List<Item> {
        items.removeAll(checkedItems)
        return items
    }

    companion object {
        const val SIZE = 50
    }
}