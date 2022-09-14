package com.example.fakestore.model.domain

import java.math.BigDecimal


data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: BigDecimal,
    val rating: Double,
    val title: String
)


