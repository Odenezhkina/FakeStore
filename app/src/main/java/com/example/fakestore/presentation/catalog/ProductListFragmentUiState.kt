package com.example.fakestore.presentation.catalog

import com.example.fakestore.presentation.model.UiFilter
import com.example.fakestore.presentation.model.UiProduct

sealed interface ProductListFragmentUiState {
    // todo fix filters
    data class Success(
        val filters: Set<UiFilter>,
        val products: List<UiProduct>
    ) : ProductListFragmentUiState

    object Loading: ProductListFragmentUiState
}
