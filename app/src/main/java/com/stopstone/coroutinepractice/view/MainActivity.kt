package com.stopstone.coroutinepractice.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stopstone.coroutinepractice.R
import com.stopstone.coroutinepractice.data.model.Item
import com.stopstone.coroutinepractice.databinding.ActivityMainBinding
import com.stopstone.coroutinepractice.listener.OnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListener {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter: MainAdapter by lazy { MainAdapter(this) }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setLayout()

        binding.btnMainSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val items = when (isChecked) {
                true -> viewModel.items.value?.filter { it.checked }
                false -> viewModel.items.value?.filter { !it.checked }
            }
            items?.let { adapter.submitList(it) }
        }
    }

    override fun onClickItem(item: Item) {
        viewModel.toggleItemState(item)
    }

    private fun setLayout() {
        binding.rvMainList.adapter = adapter
        viewModel.items.observe(this) {
            when(binding.btnMainSwitch.isChecked) {
                true -> adapter.submitList(it.filter { item -> item.checked })
                false -> adapter.submitList(it.filter { item -> !item.checked })
            }
        }
    }
}