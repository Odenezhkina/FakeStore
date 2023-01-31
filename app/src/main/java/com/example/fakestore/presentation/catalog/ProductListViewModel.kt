package com.example.fakestore.presentation.catalog

import androidx.lifecycle.viewModelScope
import com.example.fakestore.domain.generator.FilterGenerator
import com.example.fakestore.domain.model.Filter
import com.example.fakestore.domain.reducer.UiProductReducer
import com.example.fakestore.domain.sorting.SortManager
import com.example.fakestore.domain.updaters.FilterUpdater
import com.example.fakestore.presentation.ApplicationState
import com.example.fakestore.presentation.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Collections.max
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject constructor(
    val uiProductReducer: UiProductReducer,
    private val filterUpdater: FilterUpdater,
    private val filterGenerator: FilterGenerator,
    private val sorter: SortManager // todo
) : BaseViewModel() {

    fun init(){
        refreshProducts()
//        loadFilters()
    }

    private fun loadFilters() =
        viewModelScope.launch {
            store.update { applicationState ->
                val filters: Set<Filter> =
                    filterGenerator.generateFilters(applicationState.products)
                val maxCost: BigDecimal = max(applicationState.products.map { it.price })
                return@update applicationState.copy(
                    productFilterInfo = ApplicationState.ProductFilterInfo(
                        filterCategory = ApplicationState.ProductFilterInfo.FilterCategory(
                            filters = filters,
                            selectedFilter = applicationState.productFilterInfo.filterCategory.selectedFilter
                        ),
                        rangeSort = ApplicationState.ProductFilterInfo.RangeSort(
                            fromCost = 0.toBigDecimal(),
                            toCost = maxCost
                        )
                    )
                )
            }
        }

    fun updateSelectedFilter(filter: Filter) = viewModelScope.launch {
        store.update { applicationState ->
            return@update filterUpdater.updateSelectedCategory(applicationState, filter)
        }
    }

    fun updateSortType(sortType: ApplicationState.ProductFilterInfo.SortType) =
        viewModelScope.launch {
            store.update { applicationState ->
                return@update filterUpdater.updateSortType(applicationState, sortType)
            }
        }

    fun updateRangeSort(newFromCost: BigDecimal, newToCost: BigDecimal) = viewModelScope.launch {
        store.update { applicationState ->
            return@update filterUpdater.updateRangeSort(applicationState, newFromCost, newToCost)
        }
    }
}
