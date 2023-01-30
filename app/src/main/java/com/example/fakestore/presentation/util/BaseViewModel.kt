package com.example.fakestore.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.domain.model.Product
import com.example.fakestore.presentation.ApplicationState
import com.example.fakestore.presentation.Store
import com.example.fakestore.domain.updaters.CartUpdater
import com.example.fakestore.domain.updaters.FavUpdater
import com.example.fakestore.data.repository.ProductRepositoryImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var favUpdater: FavUpdater

    @Inject
    lateinit var cartUpdater: CartUpdater

    @Inject
    lateinit var productRepository: ProductRepositoryImpl

    fun refreshProducts() = viewModelScope.launch {
        val products: List<Product> = productRepository.fetchAllProducts()
        store.update { applicationState ->
            return@update applicationState.copy(
                products = products
            )
        }
    }

    fun updateFavoriteSet(productId: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update favUpdater.update(applicationState, productId)
        }
    }

    fun updateCartProductsId(productId: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update cartUpdater.update(applicationState, productId)
        }
    }

}

