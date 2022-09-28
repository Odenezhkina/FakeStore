package com.example.fakestore.model.domain

import com.example.fakestore.model.network.NetworkRating
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


