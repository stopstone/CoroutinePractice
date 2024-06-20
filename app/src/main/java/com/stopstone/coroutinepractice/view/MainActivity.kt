package com.stopstone.coroutinepractice.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.stopstone.coroutinepractice.data.model.Item
import com.stopstone.coroutinepractice.databinding.ActivityMainBinding
import com.stopstone.coroutinepractice.listener.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListener {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter: MainAdapter by lazy { MainAdapter(this) }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setLayout()
        setOnSwitchListener()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeCountdown()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelCountdownTimer()
        binding.tvRemoveTimer.visibility = View.GONE
    }

    override fun onRemoveItem(item: Item) {
        viewModel.toggleItemState(item)
    }

    override fun onRestoreItem(item: Item) {
        viewModel.toggleItemState(item)
        viewModel.resetCountdownTimer()
    }

    private fun setLayout() {
        binding.rvMainList.adapter = adapter
        lifecycleScope.launch {
            viewModel.items.collect { items ->
                when (binding.btnMainSwitch.isChecked) {
                    true -> adapter.submitList(items.filter { item -> item.checked })
                    false -> adapter.submitList(items.filter { item -> !item.checked })
                }
            }
        }
    }

    private fun setOnSwitchListener() {
        binding.btnMainSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val items = when (isChecked) {
                true -> {
                    viewModel.startCountdownTimer()
                    binding.tvRemoveTimer.visibility = View.VISIBLE
                    viewModel.items.value.filter { it.checked }
                }

                false -> {
                    viewModel.cancelCountdownTimer()
                    binding.tvRemoveTimer.visibility = View.GONE
                    viewModel.items.value.filter { !it.checked }
                }
            }
            items.let { adapter.submitList(it) }
        }
    }

    private suspend fun observeCountdown() {
        viewModel.countdownValue.collect { value ->
            binding.tvRemoveTimer.text = value.toString()
            if (value == MainViewModel.COUNTDOWN_END) {
                binding.tvRemoveTimer.visibility = View.GONE
                val removedCount = viewModel.getItemsSize()
                showToastMessage("${removedCount}개의 아이템 소멸")
                viewModel.deleteCheckedItems()
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}