package com.kotlinpractice.blinkitclone.data.mapper

import com.kotlinpractice.blinkitclone.data.local.entity.CategoryEntity
import com.kotlinpractice.blinkitclone.data.model.Category
import com.kotlinpractice.blinkitclone.data.remote.dto.CategoryDto

fun CategoryDto.categoryDtoToCategory(): Category{
    return Category(
        slug = slug,
        name = name,
        url = url,
        isSelected = false
    )
}

//With Room:

//CategoryDto (From API) to Room CategoryEntity (For CategoryEntity)
fun CategoryDto.categoryDtoToCategoryRoomEntity() = CategoryEntity(
    slug = slug,
    name = name,
    url = url
)

//Room Category Entity (Which was converted from CategoryDto) to Category which UI can use.
fun CategoryEntity.categoryEntityToCategory(): Category{
    return Category(
        slug = slug,
        name = name,
        url = url,
        isSelected = false
    )
}