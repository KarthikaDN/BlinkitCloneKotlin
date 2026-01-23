package com.kotlinpractice.blinkitclone.data.remote

import com.kotlinpractice.blinkitclone.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductsResponseDto
}