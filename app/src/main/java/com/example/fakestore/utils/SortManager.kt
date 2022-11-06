package com.example.fakestore.utils

import com.example.fakestore.R
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.redux.ApplicationState
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
        filtersInfo.sortType.sortTypeId?.let {
            resList = sortByType(it, resList)
        }

        if (filtersInfo.rangeSort.isSortActive) {
            filtersInfo.rangeSort.toCost?.let {
                resList = sortByCostRange(filtersInfo.rangeSort.fromCost, it, resList)
            }
        }

        return resList
    }

    private fun sortByType(sortType: Int, resList: List<UiProduct>): List<UiProduct> {
        return when (sortType) {
            R.id.most_popular -> sortByPopularity(resList)
            R.id.cheapest -> sortCheapestFirst(resList)
            R.id.most_expensive -> sortMostExpensiveFirst(resList)
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
