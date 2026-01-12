package com.kotlinpractice.blinkitclone.data.repository

import com.kotlinpractice.blinkitclone.data.api.RetrofitInstance

class ProductRepository {

    suspend fun getProducts() =
        RetrofitInstance.api.getProducts()
}
