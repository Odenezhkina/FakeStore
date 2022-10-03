package com.example.fakestore.model.ui

import com.example.fakestore.model.domain.Product

data class UiProduct(
    val product: Product,
    val isInFavorites: Boolean,
    val isInCart: Boolean
)
