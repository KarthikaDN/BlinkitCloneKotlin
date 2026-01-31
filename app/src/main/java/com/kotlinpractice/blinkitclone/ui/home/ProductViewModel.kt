package com.kotlinpractice.blinkitclone.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.kotlinpractice.blinkitclone.data.mapper.toDomain
import com.kotlinpractice.blinkitclone.data.model.Category
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.repository.ProductRepository
import com.kotlinpractice.blinkitclone.ui.state.CategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    val repository: ProductRepository
) : ViewModel() {

    init {
        loadCategories()
    }

    //Category Ui State
    private val _categoryState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val categoryState: StateFlow<CategoryUiState> = _categoryState

    //Categories change event tracker
    private val _onChangeCategory = MutableStateFlow<List<Category>>(emptyList())
    val onChangeCategory:StateFlow<List<Category>> = _onChangeCategory

    private val _categoriesLoaded = MutableStateFlow(false)

    //Selected Category
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory
    fun selectCategory(category: Category) {
        _onChangeCategory.value =
            _onChangeCategory.value.map {
                it.copy(isSelected = it.name == category.name)
            }

        _selectedCategory.value = category.name
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categoryState.value = CategoryUiState.Loading

            try {
                val result = repository.getCategories()

                val categories =
                    listOf(Category("all", "All", "", true)) +
                            result.map { it.toDomain() }

                _categoryState.value = CategoryUiState.Success(categories)
                _onChangeCategory.value = categories
                _categoriesLoaded.value = true

            } catch (e: Exception) {
                _categoryState.value =
                    CategoryUiState.Error(
                        e.message ?: "Failed to load categories"
                    )
            }
        }
    }

    val products: Flow<PagingData<Product>> =
        combine(
            _categoriesLoaded,
            selectedCategory
        ) { loaded, category ->
            loaded to category
        }
            .filter { (loaded, _) -> loaded }   // 🚪 gate
            .flatMapLatest { (_, category) ->
                repository.getPagedProductsByCategory(
                    if (category == "All") null else category
                )
            }
            .cachedIn(viewModelScope)
}
