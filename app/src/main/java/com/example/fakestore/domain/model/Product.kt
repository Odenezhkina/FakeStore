package com.example.fakestore.domain.model

import com.example.fakestore.data.model.NetworkRating
import java.math.BigDecimal

data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: BigDecimal,
    val rating: NetworkRating,
    val title: String
)


