package com.kotlinpractice.blinkitclone.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kotlinpractice.blinkitclone.data.mapper.toDomain
import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.remote.ProductApi

class ProductPagingSource(
    private val api: ProductApi
) : PagingSource<Int, Product>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Product> {
        return try {
            val skip = params.key ?: 1
            val response = api.getProducts(skip, params.loadSize)

            val products = response.products.map { it.toDomain() }

            LoadResult.Page(
                data = products,
                prevKey = null,
                nextKey = if (products.isEmpty()) null else skip + params.loadSize
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

