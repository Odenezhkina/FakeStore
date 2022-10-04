package com.example.fakestore.redux.reducer

import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.model.ui.CartUiProduct
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartProductReducer @Inject constructor() {

    fun reduce(store: Store<ApplicationState>): Flow<List<CartUiProduct>> {
        return store.stateFlow.run {
            combine(map { it.products },
                map { it.favoriteProductsIds },
                map { it.productCartInfo })
            { listProducts, listFavorites, productCartInfo ->

                if (listProducts.isEmpty()) {
                    return@combine emptyList<CartUiProduct>()
                }

                listProducts.map { product ->
                    UiProduct(
                        product = product,
                        isInFavorites = listFavorites.contains(product.id),
                        isInCart = productCartInfo.isInCart(product.id)
                    )
                }.map { CartUiProduct(it, productCartInfo.getQuantity(it.product.id)) }
            }
        }

    }


}
