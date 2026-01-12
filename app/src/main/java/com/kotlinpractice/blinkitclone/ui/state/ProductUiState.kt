package com.kotlinpractice.blinkitclone.ui.state

import com.kotlinpractice.blinkitclone.data.model.Product

data class ProductUiState(
    val isLoading: Boolean = false,
    val products:List<Product> = emptyList(),
    val error:String? = null
)