package com.example.fakestore.states

import com.example.fakestore.model.ui.CartUiProduct

sealed interface CartFragmentUiState {

    data class NonEmpty(
        val products: List<CartUiProduct>
    ) : CartFragmentUiState

    object Empty : CartFragmentUiState
}
