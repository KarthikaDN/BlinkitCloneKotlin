package com.kotlinpractice.blinkitclone.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.kotlinpractice.blinkitclone.data.mapper.categoryDtoToCategory
import com.kotlinpractice.blinkitclone.data.mapper.categoryEntityToCategory
import com.kotlinpractice.blinkitclone.data.mapper.toDomain
import com.kotlinpractice.blinkitclone.data.model.Category
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.repository.ProductRepository
import com.kotlinpractice.blinkitclone.ui.state.CategorySyncState
import com.kotlinpractice.blinkitclone.ui.state.CategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    val repository: ProductRepository
) : ViewModel() {

    init {
        syncCategories()
    }

    fun syncCategories(){
        viewModelScope.launch {
            repository.syncCategories()
        }
    }

    //Selected Category
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory
    fun selectCategory(category: Category) {
        _selectedCategory.value = category.name
    }

    val categoriesUiState: StateFlow<CategoryUiState> =
        combine(
            repository.categorySyncState,
            repository.getCategoriesFromRoom(),
            selectedCategory
        )
        {
            syncState,roomCategoryEntites,selected ->

            if(syncState == CategorySyncState.Error && roomCategoryEntites.isEmpty()){
                return@combine CategoryUiState.Error(
                    "Unable to load categories. Please check your internet connection."
                )
            }

            val allCategory = Category("all", "All", "", selected == "All")

            val categories = listOf(allCategory) +
                    roomCategoryEntites.map {
                        it.categoryEntityToCategory().copy(isSelected = it.name == selected)
                    }
            CategoryUiState.Success(categories)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CategoryUiState.Loading
            )

    val products: Flow<PagingData<Product>> =
        combine(
            categoriesUiState,
            selectedCategory
        ) { categoriesUiState, category ->
            categoriesUiState to category
        }
            .filter { (categoriesUiState, _) -> categoriesUiState is CategoryUiState.Success && categoriesUiState.categories.isNotEmpty() }   // 🚪 gate
            .flatMapLatest { (_, category) ->
                repository.getPagedProductsByCategory(
                    if (category == "All") null else category
                )
            }
            .cachedIn(viewModelScope)
}
