package com.example.fakestore.redux

import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val favoriteProductsIds: Set<Int> = emptySet(),
    val productCartInfo: ProductCartInfo = ProductCartInfo(),
    val productFilterInfo: ProductFilterInfo = ProductFilterInfo()
) {
    // separate class because we want to store filters (which will be displayed in the carousel)
    // and filter which is selected
    data class ProductFilterInfo(
        val filters: Set<Filter> = emptySet(),
        val selectedFilter: Filter? = null
    )

    class ProductCartInfo(
        //todo maybe it is better to use hashmap instead of map
        // we should remove elements with quantity equals 0?
        // here we control if quantity is greater than 0?
        private val mapProductIdQuantity: Map<Int, Int> = mapOf()
    ) {

        fun isInCart(productId: Int): Boolean {
            return mapProductIdQuantity.contains(productId)
        }

        fun getQuantity(productId: Int): Int {
            return mapProductIdQuantity[productId] ?: -1
        }

        fun countProductsInCart(): Int {
            return mapProductIdQuantity.size
        }

        fun getMap(): MutableMap<Int, Int> {
            return mapProductIdQuantity.toMutableMap()
        }
    }
}

