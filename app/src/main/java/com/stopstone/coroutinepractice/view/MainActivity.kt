package com.stopstone.coroutinepractice.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
                true -> {
                    viewModel.startCountdownTimer()
                    binding.tvRemoveTimer.visibility = View.VISIBLE
                    viewModel.items.value?.filter { it.checked }

                }
                false -> {
                    viewModel.cancelCountdownTimer()
                    binding.tvRemoveTimer.visibility = View.GONE
                    viewModel.items.value?.filter { !it.checked }
                }
            }
            items?.let { adapter.submitList(it) }
        }

        viewModel.countdownValue.observe(this) { value ->
            binding.tvRemoveTimer.text = value.toString()
            if (value == 0) {
                val removedCount = viewModel.items.value?.count { it.checked }
                deleteCheckedItems()
                Toast.makeText(this@MainActivity, "${removedCount}개의 아이템 소멸", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // 백그라운드에서 타이머 실행하지 않음
    override fun onPause() {
        super.onPause()
        viewModel.cancelCountdownTimer()
    }

    override fun onClickItem(item: Item) {
        viewModel.resetCountdownTimer()
        viewModel.toggleItemState(item)
    }

    private fun setLayout() {
        binding.rvMainList.adapter = adapter
        viewModel.items.observe(this) { items ->
            when (binding.btnMainSwitch.isChecked) {
                true -> adapter.submitList(items.filter { item -> item.checked })
                false -> adapter.submitList(items.filter { item -> !item.checked })
            }
        }
    }

    private fun deleteCheckedItems() {
        val checkedItems = viewModel.items.value?.filter { it.checked } ?: emptyList()
        viewModel.removeCheckedItems(checkedItems)
    }
}