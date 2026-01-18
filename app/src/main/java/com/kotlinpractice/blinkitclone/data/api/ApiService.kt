package com.kotlinpractice.blinkitclone.data.api

import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>
}