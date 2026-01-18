package com.kotlinpractice.blinkitclone.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinpractice.blinkitclone.data.repository.ProductRepository
import com.kotlinpractice.blinkitclone.ui.state.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductRepository): ViewModel() {
    private val _uiState: MutableStateFlow<ProductUiState> = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    fun fetchProducts(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try{
                val response = repository.getProducts()
                if(response.isSuccessful){
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        products = response.body()?.products ?:emptyList()
                    )
                }
                else{
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Something went wrong"
                    )
                }
            }
            catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}