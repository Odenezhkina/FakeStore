package com.example.fakestore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.domain.Product
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import com.example.fakestore.redux.updaters.CartUpdater
import com.example.fakestore.redux.updaters.FavUpdater
import com.example.fakestore.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel (
//    val store: Store<ApplicationState>,
//    private val favUpdater: FavUpdater,
//    private val cartUpdater: CartUpdater,
//    private val productRepository: ProductRepository
) : ViewModel() {

    @Inject
    lateinit var store: Store<ApplicationState>

    @Inject
    lateinit var favUpdater: FavUpdater

    @Inject
    lateinit var cartUpdater: CartUpdater

    @Inject
    lateinit var productRepository: ProductRepository

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

