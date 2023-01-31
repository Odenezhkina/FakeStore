package com.example.fakestore.data.mapper

import com.example.fakestore.presentation.util.ext.capitalize
import com.example.fakestore.data.model.NetworkProduct
import com.example.fakestore.data.model.NetworkRating
import com.example.fakestore.domain.model.Product
import java.math.BigDecimal
import java.math.RoundingMode

fun NetworkProduct.toProduct(): Product = Product(
    category.capitalize(),
    description,
    id,
    image,
    BigDecimal(price).setScale(2, RoundingMode.HALF_UP),
    NetworkRating(rating.count, rating.rate),
    title
)
