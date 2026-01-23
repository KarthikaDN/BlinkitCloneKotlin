package com.kotlinpractice.blinkitclone.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kotlinpractice.blinkitclone.data.mapper.toDomain
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.remote.ProductApi

class ProductPagingSource(
    private val api: ProductApi,
    private val category: String?
) : PagingSource<Int, Product>()
 {

     override suspend fun load(
         params: LoadParams<Int>
     ): LoadResult<Int, Product> {
         return try {
             val skip = params.key ?: 0
             val limit = params.loadSize

             val response =
                 if (category == null) {
                     api.getProducts(skip, limit)
                 } else {
                     api.getProductsByCategory(category, skip, limit)
                 }

             val products = response.products.map { it.toDomain() }

             LoadResult.Page(
                 data = products,
                 prevKey = null, // 🔴 append-only
                 nextKey = if (products.isEmpty()) null else skip + limit
             )
         } catch (e: Exception) {
             LoadResult.Error(e)
         }
     }


     override fun getRefreshKey(state: PagingState<Int, Product>): Int? =
        state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
}

