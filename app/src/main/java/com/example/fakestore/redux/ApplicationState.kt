package com.example.fakestore.redux

import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product
import java.math.BigDecimal

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val favoriteProductsIds: Set<Int> = emptySet(),
    val productCartInfo: ProductCartInfo = ProductCartInfo(),
    val productFilterInfo: ProductFilterInfo = ProductFilterInfo()
) {

    data class ProductFilterInfo(
        val filterCategory: FilterCategory = FilterCategory(),
        val rangeSort: RangeSort = RangeSort(),
        val sortType: SortType = SortType()
    ) {

        data class FilterCategory(
            val filters: Set<Filter> = emptySet(),
            val selectedFilter: Filter? = null,
        )

        data class SortType(
            val sortTypeId: Int? = null
        )

        data class RangeSort(
            val isSortActive: Boolean = false,
            val fromCost: BigDecimal = BigDecimal(0),
            val toCost: BigDecimal? = null
        )
    }

    class ProductCartInfo(
        private val mapProductIdQuantity: Map<Int, Int> = hashMapOf()
    ) {

        fun isInCart(productId: Int): Boolean {
            return mapProductIdQuantity.contains(productId)
        }

        fun getQuantity(productId: Int): Int {
            return mapProductIdQuantity[productId] ?: -1
        }

        fun countProductsInCart(): Int = mapProductIdQuantity.size

        fun getMap(): MutableMap<Int, Int> = mapProductIdQuantity.toMutableMap()
    }
}

