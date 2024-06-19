package com.stopstone.coroutinepractice.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stopstone.coroutinepractice.databinding.ItemListBinding
import com.stopstone.coroutinepractice.data.model.Item
import com.stopstone.coroutinepractice.listener.OnClickListener

class MainAdapter(private val listener: OnClickListener) :
    ListAdapter<Item, RecyclerView.ViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            VIEW_TYPE_TRASH -> TrashViewHolder(binding)
            VIEW_TYPE_ITEM -> ItemViewHolder(binding)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is TrashViewHolder -> holder.bind(item, listener)
            is ItemViewHolder -> holder.bind(item, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).checked) VIEW_TYPE_TRASH else VIEW_TYPE_ITEM
    }

    class ItemViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, listener: OnClickListener) {
            binding.tvItemAlphabet.text = "${item.id}. ${item.alphabet}"
            binding.root.setOnClickListener { listener.onRemoveItem(item) }
        }
    }

    class TrashViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, listener: OnClickListener) {
            binding.tvItemAlphabet.text = "${item.id}. ${item.alphabet}"
            Glide.with(binding.root)
                .load(TRASH_ICON)
                .into(binding.btnRestore)
            binding.btnRestore.setOnClickListener { listener.onRestoreItem(item) }
        }
    }

    companion object {
        val ItemDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }

        const val VIEW_TYPE_TRASH = 0
        const val VIEW_TYPE_ITEM = 1
        const val TRASH_ICON =
            "https://github.com/stopstone/CoroutinePractice/assets/77120604/76f35bc0-34e9-4034-ad08-48082d6f5657"
    }
}