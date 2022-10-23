package com.example.fakestore.managers

import android.util.Log
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.ApplicationState.ProductFilterInfo.SortType.Companion.SORT_TYPE_CHEAPEST_FIRST
import com.example.fakestore.redux.ApplicationState.ProductFilterInfo.SortType.Companion.SORT_TYPE_MOST_EXPENSIVE_FIRST
import com.example.fakestore.redux.ApplicationState.ProductFilterInfo.SortType.Companion.SORT_TYPE_MOST_POPULAR
import java.math.BigDecimal


class SortManager(
    private val uiProducts: List<UiProduct>,
    private val filtersInfo: ApplicationState.ProductFilterInfo
) {

    fun sort(): List<UiProduct> {
        var resList = uiProducts

        if (filtersInfo.filterCategory.selectedFilter != null) {
            resList = sortByCategory(filtersInfo.filterCategory.selectedFilter.title, resList)
        }

        // todo why is active, remove is active
        filtersInfo.sortType.sortType?.let {
            resList = sortByType(it, resList)
            Log.d("TAGTAG", "type done")
        }

        if (filtersInfo.rangeSort.isSortActive) {
            filtersInfo.rangeSort.toCost?.let {
                resList = sortByCostRange(filtersInfo.rangeSort.fromCost, it, resList)
            }
        }
        Log.d("TAGTAG", "Done")

        return resList
    }

    private fun sortByType(sortType: Int, resList: List<UiProduct>): List<UiProduct> {
        return when (sortType) {
            SORT_TYPE_MOST_POPULAR -> sortByPopularity(resList)
            SORT_TYPE_CHEAPEST_FIRST -> sortCheapestFirst(resList)
            SORT_TYPE_MOST_EXPENSIVE_FIRST -> sortMostExpensiveFirst(resList)
            else -> emptyList()
        }
    }

    private fun sortByPopularity(resList: List<UiProduct>): List<UiProduct> {
        return resList
            .sortedByDescending { it.product.rating.rate }
            .sortedByDescending { it.product.rating.count }
    }

    private fun sortCheapestFirst(resList: List<UiProduct>): List<UiProduct> {
        return resList.sortedBy { it.product.price }
    }

    private fun sortMostExpensiveFirst(resList: List<UiProduct>): List<UiProduct> {
        return resList
            .sortedByDescending { it.product.price }
    }

    private fun sortByCostRange(
        from: BigDecimal,
        to: BigDecimal,
        resList: List<UiProduct>
    ): List<UiProduct> {
        return resList
            .filter { it.product.price in from..to }
    }

    private fun sortByCategory(category: String, resList: List<UiProduct>): List<UiProduct> {
        return resList
            .filter { uiProduct ->
                uiProduct.product.category == category
            }
    }
}
