package com.kotlinpractice.blinkitclone.data.remote.dto

data class ProductsResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)