package com.example.fakestore.data.model

data class NetworkProduct(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: NetworkRating,
    val title: String
)
