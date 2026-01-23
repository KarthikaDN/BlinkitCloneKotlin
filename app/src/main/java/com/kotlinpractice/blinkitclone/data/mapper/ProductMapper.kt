package com.kotlinpractice.blinkitclone.data.mapper

import com.kotlinpractice.blinkitclone.data.model.Product
import com.kotlinpractice.blinkitclone.data.remote.dto.ProductDto

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        thumbnail = thumbnail,
        images = images
    )
}

