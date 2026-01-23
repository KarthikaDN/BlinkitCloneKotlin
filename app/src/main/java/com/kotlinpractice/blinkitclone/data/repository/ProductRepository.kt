package com.kotlinpractice.blinkitclone.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kotlinpractice.blinkitclone.data.paging.ProductPagingSource
import com.kotlinpractice.blinkitclone.data.remote.ProductApi
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.remote.dto.CategoryDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductApi
) {

    suspend fun getCategories(): List<CategoryDto> = api.getCategories()

    fun getPagedProductsByCategory(category: String?): Flow<PagingData<Product>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductPagingSource(api, category)
            }
        ).flow

}


