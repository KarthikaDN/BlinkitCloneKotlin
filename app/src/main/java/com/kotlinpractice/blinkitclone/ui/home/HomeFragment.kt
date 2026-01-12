package com.kotlinpractice.blinkitclone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.kotlinpractice.blinkitclone.R
import com.kotlinpractice.blinkitclone.databinding.FragmentHomeBinding
import com.kotlinpractice.blinkitclone.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment(){
    private var binding: FragmentHomeBinding? = null
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProducts();
    }
    private fun observeProducts(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if(state.isLoading){

                }
                else if(state.error != null){

                }
                else{
                    
                }
            }
        }
    }
}