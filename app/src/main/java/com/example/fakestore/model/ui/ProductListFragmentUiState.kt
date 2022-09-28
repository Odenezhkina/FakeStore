package com.example.fakestore.model.ui

data class ProductListFragmentUiState(
    val filters: Set<UiFilter>,
    val products: List<UiProduct>
)
