package com.example.fakestore.presentation.cart

import com.example.fakestore.presentation.model.CartUiProduct

sealed interface CartFragmentUiState {

    data class NonEmpty(
        val products: List<CartUiProduct>
    ) : CartFragmentUiState

    object Empty : CartFragmentUiState
}
