package com.kotlinpractice.blinkitclone.data.api

import com.kotlinpractice.blinkitclone.data.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProducts(): Response<List<Product>>
}