package com.kotlinpractice.blinkitclone.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinpractice.blinkitclone.R
import com.kotlinpractice.blinkitclone.databinding.FragmentHomeBinding
import com.kotlinpractice.blinkitclone.ui.state.CategoryUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductPagingAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val itemWidthDp = 160 // minimum item width

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupCategoryRecyclerView();
        setupProductRecyclerView()

        observeCategoryData()
//        observeCategoryClickEvent()

        observeProductPagingData()
        observeProductLoadState()

        setupRetry()
    }

    // ---------------- RecyclerView ----------------

    private fun setupCategoryRecyclerView(){
        categoryAdapter = CategoryAdapter { category ->
            viewModel.selectCategory(category)
        }

        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = categoryAdapter
        }
    }

    private fun setupProductRecyclerView() {
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

    private fun observeCategoryData(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoriesUiState.collect { state ->
                    when (state) {
                        is CategoryUiState.Loading -> {
                            binding.shimmer.isVisible = true
                        }

                        is CategoryUiState.Success -> {
                            binding.shimmer.isVisible = false
                            categoryAdapter.submitList(state.categories)
                        }

                        is CategoryUiState.Error -> {
                            binding.shimmer.isVisible = false
                            binding.errorLayout.isVisible = true
                            binding.errorText.text = state.message
                        }
                    }
                }
            }
        }

    }

//    private fun observeCategoryClickEvent(){
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED){
//                viewModel.onChangeCategory.collect {
//                    categoryAdapter.submitList(it)
//                }
//            }
//        }
//    }
    private fun observeProductPagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun observeProductLoadState() {
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
            viewModel.syncCategories()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
