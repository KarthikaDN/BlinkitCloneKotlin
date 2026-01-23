package com.kotlinpractice.blinkitclone.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    repository: ProductRepository
) : ViewModel() {

    val products: Flow<PagingData<Product>> =
        repository.getPagedProducts()
            .cachedIn(viewModelScope)
}
