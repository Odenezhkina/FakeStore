package com.example.fakestore.domain.reducer

import com.example.fakestore.presentation.model.UiProduct
import com.example.fakestore.presentation.ApplicationState
import com.example.fakestore.presentation.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UiProductReducer @Inject constructor() {

    fun reduce(store: Store<ApplicationState>): Flow<List<UiProduct>> {
        return store.stateFlow.run {
            combine(map { it.products },
                map { it.favoriteProductsIds },
                map { it.productCartInfo })
            { listProducts, listFavorites, productCartInfo ->

                if (listProducts.isEmpty()) {
                    return@combine emptyList<UiProduct>()
                }

                return@combine listProducts.map { product ->
                    UiProduct(
                        product = product,
                        isInFavorites = listFavorites.contains(product.id),
                        isInCart = productCartInfo.isInCart(product.id)
                    )
                }
            }
        }
    }
}
