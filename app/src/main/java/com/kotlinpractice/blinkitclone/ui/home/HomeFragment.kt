package com.kotlinpractice.blinkitclone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinpractice.blinkitclone.R
import com.kotlinpractice.blinkitclone.databinding.FragmentHomeBinding
import com.kotlinpractice.blinkitclone.ui.state.ProductUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter
    private val ItemWidthDp = 160 //(This is a minimum width, not a fixed width)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ INITIALIZATION HERE
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        observeUiState()
        retryProductsLoading()

        viewModel.fetchProducts()
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter()
        val spanCount = calculateSpanCount()
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),spanCount)
            adapter = this@HomeFragment.adapter
        }
    }
    private fun calculateSpanCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return maxOf(2, (screenWidthDp / ItemWidthDp).toInt())
    }
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading

                    if (state.error != null) {
                        binding.errorLayout.isVisible = true
                    }
                    else{
                        binding.errorLayout.isVisible = false
                        adapter.submitList(state.products)
                    }
                }
            }
        }
    }

    private fun retryProductsLoading(){
        binding.btnRetry.setOnClickListener {
            viewModel.fetchProducts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ✅ VERY IMPORTANT (avoid memory leaks)
        _binding = null
    }
}
