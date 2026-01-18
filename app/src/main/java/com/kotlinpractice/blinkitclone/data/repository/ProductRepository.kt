package com.kotlinpractice.blinkitclone.data.repository

import com.kotlinpractice.blinkitclone.data.api.ApiService
import retrofit2.Retrofit
import javax.inject.Inject

class ProductRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getProducts() = apiService.getProducts()
}
