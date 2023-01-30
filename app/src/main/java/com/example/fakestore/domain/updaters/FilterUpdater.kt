package com.example.fakestore.domain.updaters

import android.util.Log
import com.example.fakestore.domain.model.Filter
import com.example.fakestore.presentation.ApplicationState
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
//        return applicationState.copy(
//            productFilterInfo = ApplicationState.ProductFilterInfo(
//                filterCategory = ApplicationState.ProductFilterInfo.FilterCategory(selectedFilter = newFilter)
//            )
//        )
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
//        return applicationState.copy(
//            productFilterInfo = ApplicationState.ProductFilterInfo(
//                rangeSort = ApplicationState.ProductFilterInfo.RangeSort(true, from, to)
//            )
//        )
    }

    fun updateSortType(applicationState: ApplicationState, sortType: Int): ApplicationState {
        // if sort type is already selected -> set isActive as false
        // else -> set sortType
        val newSortType = if (applicationState.productFilterInfo.sortType.sortTypeId == sortType) {
            null
        } else {
            sortType
        }
        Log.d("TAGTAG", "$javaClass : updating sort type ")
        return applicationState.copy(
            productFilterInfo = applicationState
                .productFilterInfo
                .copy(sortType = ApplicationState.ProductFilterInfo.SortType(newSortType))
        )
//        return applicationState.copy(
//            productFilterInfo = ApplicationState.ProductFilterInfo(sortType =  ApplicationState.ProductFilterInfo.SortType(newSortType)
//            )
//        )
    }
}
