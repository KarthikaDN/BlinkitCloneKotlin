package com.kotlinpractice.blinkitclone.data.remote.dto

data class ProductDto(
    val id: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val images: List<String>
)