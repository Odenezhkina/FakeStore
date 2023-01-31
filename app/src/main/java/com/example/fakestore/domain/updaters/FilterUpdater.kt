package com.example.fakestore.domain.updaters

import com.example.fakestore.domain.model.Filter
import com.example.fakestore.presentation.ApplicationState
import com.example.fakestore.presentation.ApplicationState.ProductFilterInfo.SortType
import java.math.BigDecimal
import javax.inject.Inject

class FilterUpdater @Inject constructor() {

    fun updateSelectedCategory(applicationState: ApplicationState, filter: Filter): ApplicationState {
        // if it already selected -> remove
        // else -> replace
        var newFilter: Filter? = filter
        if (applicationState.productFilterInfo.filterCategory.selectedFilter == filter) {
            newFilter = null
        }
        return applicationState.copy(
            productFilterInfo = ApplicationState.ProductFilterInfo(
                filterCategory = ApplicationState.ProductFilterInfo.FilterCategory(selectedFilter = newFilter)
            )
        )
    }

    fun updateRangeSort(applicationState: ApplicationState, from: BigDecimal, to: BigDecimal): ApplicationState {
        // replacing with new values
        applicationState.productFilterInfo.rangeSort.run {
            if (from == fromCost && to == toCost) {
                return applicationState
            }
        }
        return applicationState.copy(
            productFilterInfo = applicationState.productFilterInfo.copy(
                rangeSort = ApplicationState.ProductFilterInfo.RangeSort(true, from, to)
            )
        )
    }

    fun updateSortType(applicationState: ApplicationState, sortType: SortType): ApplicationState {
        // if sort type is already selected -> set isActive as false
        // else -> set sortType
        val newSortType = if (applicationState.productFilterInfo.sortType == sortType) {
            SortType.DEFAULT
        } else {
            sortType
        }
        return applicationState.copy(
            productFilterInfo = applicationState
                .productFilterInfo
                .copy(sortType = newSortType)
        )
    }
}
