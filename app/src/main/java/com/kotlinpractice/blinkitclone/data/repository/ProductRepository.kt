package com.kotlinpractice.blinkitclone.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kotlinpractice.blinkitclone.data.local.dao.CategoryDao
import com.kotlinpractice.blinkitclone.data.local.entity.CategoryEntity
import com.kotlinpractice.blinkitclone.data.mapper.categoryDtoToCategoryRoomEntity
import com.kotlinpractice.blinkitclone.data.mapper.categoryEntityToCategory
import com.kotlinpractice.blinkitclone.data.model.Category
import com.kotlinpractice.blinkitclone.data.paging.ProductPagingSource
import com.kotlinpractice.blinkitclone.data.remote.ProductApi
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.remote.dto.CategoryDto
import com.kotlinpractice.blinkitclone.ui.state.CategorySyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductApi,
    private val categoryDao: CategoryDao
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

    //Room related

    private val _categorySyncState = MutableStateFlow<CategorySyncState>(CategorySyncState.Idle)
    val categorySyncState: StateFlow<CategorySyncState> = _categorySyncState

    suspend fun syncCategories() {
        _categorySyncState.value = CategorySyncState.Loading

        try {
            val remote = api.getCategories()
            categoryDao.insertAll(remote.map { it.categoryDtoToCategoryRoomEntity() })
            _categorySyncState.value = CategorySyncState.Idle
        } catch (e: Exception) {
            _categorySyncState.value = CategorySyncState.Error
        }
    }

    fun getCategoriesFromRoom(): Flow<List<CategoryEntity>> =
        categoryDao.observeCategories()
}


