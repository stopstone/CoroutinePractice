package com.stopstone.coroutinepractice.data.repository

import com.stopstone.coroutinepractice.data.model.Item
import javax.inject.Inject

class MainRepository @Inject constructor() {
    private val items = mutableListOf<Item>()

    fun generateData(): List<Item> {
        items.apply {
            repeat(SIZE) {
                val shuffledAlphabet = ALPHABET.toList().shuffled()
                add(Item("${it + 1}. " + shuffledAlphabet.first()))
            }
        }
        return items
    }

    fun removeItem(item: Item): MutableList<Item> {
        items.removeAll(listOf(item))
        return items
    }

    fun toggleItem(item: Item): MutableList<Item> {
        items.add(item.copy(checked = !item.checked))
        return items
    }

    fun removeCheckedItems(checkedItems: List<Item>): MutableList<Item> {
        items.removeAll(checkedItems)
        return items
    }

    companion object {
        const val SIZE = 50
        const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }
}