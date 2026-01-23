package com.kotlinpractice.blinkitclone.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.kotlinpractice.blinkitclone.R
import com.kotlinpractice.blinkitclone.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductPagingAdapter

    private val itemWidthDp = 160 // minimum item width

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        observePagingData()
        observeLoadState()
        setupRetry()
    }

    // ---------------- RecyclerView ----------------

    private fun setupRecyclerView() {
        adapter = ProductPagingAdapter()

        val spanCount = calculateSpanCount()
        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = this@HomeFragment.adapter
        }
    }

    private fun calculateSpanCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return maxOf(2, (screenWidthDp / itemWidthDp).toInt())
    }

    // ---------------- Paging ----------------

    private fun observePagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { state ->
                binding.apply {
                    shimmer.isVisible = state.refresh is LoadState.Loading
                    errorLayout.isVisible = state.refresh is LoadState.Error
                    emptyView.isVisible =
                        state.refresh is LoadState.NotLoading &&
                                adapter.itemCount == 0
                }
            }
        }
    }

    private fun setupRetry() {
        binding.btnRetry.setOnClickListener {
            adapter.retry()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
