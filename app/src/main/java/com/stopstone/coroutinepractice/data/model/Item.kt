package com.stopstone.coroutinepractice.data.model

data class Item(
    val id: Int,
    val alphabet: Char,
    val checked: Boolean = false,
)