package com.stopstone.coroutinepractice.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stopstone.coroutinepractice.databinding.ItemListBinding
import com.stopstone.coroutinepractice.data.model.Item
import com.stopstone.coroutinepractice.listener.OnClickListener

class MainAdapter(private val listener: OnClickListener) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>() {
    private val items = mutableListOf<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], listener)

    }

    override fun getItemCount(): Int = items.size

    fun submitList(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ItemViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, listener: OnClickListener) {
            binding.tvItemAlphabet.text = item.alphabet;
            Glide.with(binding.root)
                .load(TRASH_ICON)
                .into(binding.btnItemToggleDeleteRestore)

            binding.btnItemToggleDeleteRestore.setOnClickListener {
                listener.onClickItem(item)
            }
        }
    }



    companion object {
        const val TRASH_ICON = "https://github.com/stopstone/CoroutinePractice/assets/77120604/76f35bc0-34e9-4034-ad08-48082d6f5657"
    }
}