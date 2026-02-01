package com.kotlinpractice.blinkitclone.data.remote

import com.kotlinpractice.blinkitclone.data.remote.dto.CategoryDto
import com.kotlinpractice.blinkitclone.data.remote.dto.ProductsResponseDto
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductsResponseDto

    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductsResponseDto
}