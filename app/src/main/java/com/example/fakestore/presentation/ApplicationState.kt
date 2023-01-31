package com.example.fakestore.presentation

import androidx.annotation.IdRes
import com.example.fakestore.R
import com.example.fakestore.domain.model.Filter
import com.example.fakestore.domain.model.Product
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
        val sortType: SortType = SortType.DEFAULT
    ) {

        data class FilterCategory(
            val filters: Set<Filter> = emptySet(),
            val selectedFilter: Filter? = null,
        )

        //        data class SortType(
//            val sortTypeId: Int? = null
//        )
        enum class SortType {
            BY_POPULARITY, CHEAPEST_FIRST, MOST_EXPENSIVE_FIRST, DEFAULT
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
            return mapProductIdQuantity[productId] ?: 1
        }

        fun countProductsInCart(): Int = mapProductIdQuantity.size

        fun getMap(): MutableMap<Int, Int> = mapProductIdQuantity.toMutableMap()
    }
}

