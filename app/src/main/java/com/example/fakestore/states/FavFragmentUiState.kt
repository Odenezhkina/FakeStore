package com.example.fakestore.states

import com.example.fakestore.model.ui.UiProduct

sealed interface FavFragmentUiState {
    data class NonEmpty(
        val products: List<UiProduct>
    ) : FavFragmentUiState

    object Empty : FavFragmentUiState
}
