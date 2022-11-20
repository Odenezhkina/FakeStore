package com.example.fakestore.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.fakestore.redux.reducer.CartProductReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
//    val store: Store<ApplicationState>,
//    private val favUpdater: FavUpdater,
//    private val cartUpdater: CartUpdater,
    val uiProductDetailedReducer: CartProductReducer
) : BaseViewModel() {

//    fun updateFavoriteSet(productId: Int) = viewModelScope.launch {
//        store.update { applicationState ->
//            return@update favUpdater.update(applicationState, productId)
//        }
//    }
//
//    fun updateCartProductsId(productId: Int) = viewModelScope.launch {
//        store.update { applicationState ->
//            return@update cartUpdater.update(applicationState, productId)
//        }
//    }

    fun updateCartQuantity(productId: Int, updatedQuantity: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update cartUpdater.updateWithQuantity(
                applicationState,
                productId,
                updatedQuantity
            )
        }
    }

}
