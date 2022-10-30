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
        val filterRange: FilterRange = FilterRange(),
        val rangeSort: RangeSort = RangeSort(),
        val sortType: SortType = SortType()
    ) {

        data class FilterCategory(
            val filters: Set<Filter> = emptySet(),
            val selectedFilter: Filter? = null,
        )

        data class  FilterRange(
            val filters: Set<Filter> = emptySet(),
            val isInRangeFilter: Filter? = null,
        )

        data class SortType(
            val sortType: Int? = null
        ) {
            companion object {
                const val SORT_TYPE_MOST_POPULAR = 1
                const val SORT_TYPE_CHEAPEST_FIRST = 2
                const val SORT_TYPE_MOST_EXPENSIVE_FIRST = 3
            }
        }

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

