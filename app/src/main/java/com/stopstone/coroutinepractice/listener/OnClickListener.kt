package com.stopstone.coroutinepractice.listener

import com.stopstone.coroutinepractice.data.model.Item

interface OnClickListener {
    fun onClickItem(item: Item)
}