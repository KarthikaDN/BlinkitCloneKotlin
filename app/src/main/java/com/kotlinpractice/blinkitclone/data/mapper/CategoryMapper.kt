package com.kotlinpractice.blinkitclone.data.mapper

import com.kotlinpractice.blinkitclone.data.model.Category
import com.kotlinpractice.blinkitclone.data.remote.dto.CategoryDto

fun CategoryDto.toDomain(): Category{
    return Category(
        slug = slug,
        name = name,
        url = url,
        isSelected = false
    )
}