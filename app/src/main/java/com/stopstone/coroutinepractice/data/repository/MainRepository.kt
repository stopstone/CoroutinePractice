package com.stopstone.coroutinepractice.data.repository

import com.stopstone.coroutinepractice.data.model.Item
import javax.inject.Inject

class MainRepository @Inject constructor() {
    fun generateData(): List<Item> {
        val items = mutableListOf<Item>().apply {
            repeat(SIZE) {
                val shuffledAlphabet = ALPHABET.toList().shuffled()
                add(Item("${it + 1}. " + shuffledAlphabet.first()))
            }
        }
        return items
    }

    companion object {
        const val SIZE = 50
        const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    }
}