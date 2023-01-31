package com.example.fakestore.domain.sorting

import com.example.fakestore.presentation.ApplicationState
import com.example.fakestore.presentation.ApplicationState.ProductFilterInfo.SortType
import com.example.fakestore.presentation.model.UiProduct
import java.math.BigDecimal
import javax.inject.Inject


class SortManager @Inject constructor() {

    fun sort(
        uiProducts: List<UiProduct>,
        filtersInfo: ApplicationState.ProductFilterInfo
    ): List<UiProduct> {
        var resList = uiProducts

        if (filtersInfo.filterCategory.selectedFilter != null) {
            resList = sortByCategory(filtersInfo.filterCategory.selectedFilter.title, resList)
        }

        resList = sortByType(filtersInfo.sortType, resList)

        if (filtersInfo.rangeSort.isSortActive) {
            filtersInfo.rangeSort.toCost?.let {
                resList = sortByCostRange(filtersInfo.rangeSort.fromCost, it, resList)
            }
        }

        return resList
    }

    private fun sortByType(
        sortType: SortType,
        resList: List<UiProduct>
    ): List<UiProduct> {
        return when (sortType) {
            SortType.BY_POPULARITY -> sortByPopularity(resList)
            SortType.CHEAPEST_FIRST -> sortCheapestFirst(resList)
            SortType.MOST_EXPENSIVE_FIRST -> sortMostExpensiveFirst(resList)
            SortType.DEFAULT -> resList
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
