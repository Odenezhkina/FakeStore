package com.example.fakestore.model.network

data class NetworkProduct(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: NetworkRating,
    val title: String
)
