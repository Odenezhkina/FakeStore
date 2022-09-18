package com.example.fakestore.redux

import com.example.fakestore.model.domain.Product

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val favorites: Set<Int> = emptySet()
)
