package com.example.fakestore.states

import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.model.ui.UiProduct

sealed interface ProductListFragmentUiState {
    // todo fix filters
    data class Success(
        val filters: Set<UiFilter>,
        val products: List<UiProduct>
    ) : ProductListFragmentUiState

    object Loading: ProductListFragmentUiState
}
