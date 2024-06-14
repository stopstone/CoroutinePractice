package com.stopstone.coroutinepractice.data.repository

import com.stopstone.coroutinepractice.data.model.Item
import javax.inject.Inject

class MainRepository @Inject constructor() {
    private val items = mutableListOf<Item>()
    private val trashItems = mutableListOf<Item>()

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
        trashItems.add(item.copy(checked = !item.checked))
        return trashItems
    }

    companion object {
        const val SIZE = 50
        const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }
}