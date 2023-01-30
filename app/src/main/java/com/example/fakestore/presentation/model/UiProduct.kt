package com.example.fakestore.presentation.model

import com.example.fakestore.domain.model.Product

data class UiProduct(
    val product: Product,
    val isInFavorites: Boolean,
    val isInCart: Boolean
)
