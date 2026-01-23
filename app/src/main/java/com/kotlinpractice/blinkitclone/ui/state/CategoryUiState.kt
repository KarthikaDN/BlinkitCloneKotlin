package com.kotlinpractice.blinkitclone.ui.state

import com.kotlinpractice.blinkitclone.data.model.Category

sealed class CategoryUiState {
    object Loading : CategoryUiState()
    data class Success(val categories: List<Category>) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}
