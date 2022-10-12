package com.example.fakestore.managers

import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.redux.ApplicationState
import java.math.BigDecimal

class SortManager(
    private val uiProducts: List<UiProduct>,
    private val filtersInfo: ApplicationState.ProductFilterInfo
) {

    fun sort(): List<UiProduct> {
        filtersInfo.run {
            if(filterCategory.selectedFilter != null){
                sortByCategory(filterCategory.selectedFilter.title)
            }

            if (sortType.isSortActive){
                sortType.sortType?.let { sortByType(it) }
            }

            if(rangeSort.isSortActive){
                // todo what if toCost is null
                rangeSort.toCost?.let { sortByCostRange(rangeSort.fromCost, it)}
            }
        }
        return uiProducts
    }

    private fun sortByType(sortType: Int): List<UiProduct> {
        return when (sortType) {
            ApplicationState.ProductFilterInfo.SortType.SORT_TYPE_MOST_POPULAR -> sortByPopularity()
            ApplicationState.ProductFilterInfo.SortType.SORT_TYPE_CHEAPEST_FIRST -> sortCheapestFirst()
            ApplicationState.ProductFilterInfo.SortType.SORT_TYPE_MOST_EXPENSIVE_FIRST -> sortMostExpensiveFirst()
            else -> emptyList() // todo throw some error
        }
    }

    private fun sortByPopularity(): List<UiProduct> {
        // todo check if logic is correct
        return uiProducts
            .sortedByDescending { it.product.rating.rate }
            .sortedByDescending { it.product.rating.count }
    }

    private fun sortCheapestFirst(): List<UiProduct> {
        return uiProducts
            .sortedBy { it.product.price}
    }

    private fun sortMostExpensiveFirst(): List<UiProduct> {
        return uiProducts
            .sortedByDescending { it.product.price}
    }

    private fun sortByCostRange(from: BigDecimal, to: BigDecimal): List<UiProduct> {
        return uiProducts
            .filter { it.product.price in from..to }
    }

    private fun sortByCategory(category: String): List<UiProduct> {
        return uiProducts
            .filter { uiProduct ->
                uiProduct.product.category == category
            }
    }

}
