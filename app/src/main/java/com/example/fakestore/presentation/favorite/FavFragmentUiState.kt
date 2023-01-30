package com.example.fakestore.presentation.favorite

import com.example.fakestore.presentation.model.UiProduct

sealed interface FavFragmentUiState {
    data class NonEmpty(
        val products: List<UiProduct>
    ) : FavFragmentUiState

    object Empty : FavFragmentUiState
}
