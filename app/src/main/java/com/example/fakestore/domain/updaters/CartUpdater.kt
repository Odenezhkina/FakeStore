package com.example.fakestore.domain.updaters

import com.example.fakestore.presentation.ApplicationState
import javax.inject.Inject

class CartUpdater @Inject constructor() {

    fun update(
        applicationState: ApplicationState,
        productId: Int
    ): ApplicationState {
        return applicationState.copy(
            productCartInfo = update(applicationState.productCartInfo.getMap(), productId)
            )

    }

    fun updateWithQuantity(
        applicationState: ApplicationState,
        productId: Int,
        updatedQuantity: Int
    ): ApplicationState {
        return applicationState.copy(
            productCartInfo = updateWithQuantity(
                applicationState.productCartInfo.getMap(),
                productId,
                updatedQuantity
            )
        )

    }

    private fun updateWithQuantity(
        mapProductIdQuantity: Map<Int, Int>,
        productId: Int,
        quantity: Int
    ): ApplicationState.ProductCartInfo {
        var newList = mapProductIdQuantity.toMutableMap()
        if (quantity > QUANTITY_LOWER_BOUND) {
            newList[productId] = quantity
        }
        return ApplicationState.ProductCartInfo(newList)
    }

    private fun update(
        mapProductIdQuantity: MutableMap<Int, Int>,
        productId: Int
    ): ApplicationState.ProductCartInfo {
        // if already in cart -> remove
        // not in cart yet -> add
        if (mapProductIdQuantity.contains(productId)) {
            mapProductIdQuantity.remove(productId)
        } else {
            mapProductIdQuantity.put(
                productId,
                QUANTITY_LOWER_BOUND
            )
        }
        return ApplicationState.ProductCartInfo(mapProductIdQuantity)
    }

    companion object {
        private const val QUANTITY_LOWER_BOUND = 1
    }
}
