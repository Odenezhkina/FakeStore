package com.example.fakestore.redux.updaters

import com.example.fakestore.model.domain.Filter
import com.example.fakestore.redux.ApplicationState
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
                filterCategory = ApplicationState.ProductFilterInfo.FilterCategory(filters = applicationState.productFilterInfo.filterCategory.filters,
                    selectedFilter = newFilter)
            )
        )
    }

    fun updateRangeSort(applicationState: ApplicationState, from: BigDecimal, to: BigDecimal): ApplicationState {
        // replacing with new values
        // todo manage isActive status
        applicationState.productFilterInfo.rangeSort.run {
            if(from == fromCost && to == toCost) {
                return applicationState
            }
        }
        return applicationState.copy(
            productFilterInfo = ApplicationState.ProductFilterInfo(
                rangeSort = ApplicationState.ProductFilterInfo.RangeSort(true, from, to)
            )
        )
    }

    fun updateSortType(applicationState: ApplicationState, sortType: Int): ApplicationState {
        // if sort type is already selected -> set isActive as false
        // else -> set sortType
        val isActive = applicationState.productFilterInfo.sortType.sortType != sortType
        val newSortType = if (isActive) sortType else null
        return applicationState.copy(
            productFilterInfo = ApplicationState.ProductFilterInfo(
                sortType =  ApplicationState.ProductFilterInfo.SortType(isActive, newSortType)
            )
        )
    }
}
