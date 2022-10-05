package com.example.fakestore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import com.example.fakestore.redux.reducer.CartProductReducer
import com.example.fakestore.redux.updaters.CartUpdater
import com.example.fakestore.redux.updaters.FavUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    val store: Store<ApplicationState>,
//    private val productRepository: ProductRepository, ????
    private val favUpdater: FavUpdater,
    private val cartUpdater: CartUpdater,
    val uiProductDetailedReducer: CartProductReducer
) : ViewModel() {

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

    fun updateCartQuantity(productId: Int, updatedQuantity: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update cartUpdater.update(applicationState, productId, updatedQuantity)
        }
    }

}
