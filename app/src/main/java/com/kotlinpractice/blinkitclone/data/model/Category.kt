package com.kotlinpractice.blinkitclone.data.model

data class Category(
    val slug: String,
    val name: String,
    val url: String,
    val isSelected: Boolean = false
)