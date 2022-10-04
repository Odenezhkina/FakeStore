package com.example.fakestore.model.ui

sealed interface ProductListFragmentUiState {
    // todo understand all of this...
    data class Success(
        val filters: Set<UiFilter>,
        val products: List<UiProduct>
    ) : ProductListFragmentUiState

    object Loading
}
