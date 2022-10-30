package com.example.fakestore.redux.updaters

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.fakestore.epoxy.controllers.UiFilterItemController
import com.example.fakestore.epoxy.controllers.UiProductListFragmentController
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.generator.FilterGenerator
import com.example.fakestore.states.ProductListFragmentUiState
import com.example.fakestore.viewmodels.ProductListViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject


class FilterUpdater @Inject constructor() {

    @Inject
    lateinit var uiStateGenerator: FilterGenerator

    fun updateSelectedCategory(
        applicationState: ApplicationState,
        filter: Filter
    ): ApplicationState {
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
            if (from == fromCost && to == toCost) {
                return applicationState
            }
        }
        return applicationState.copy(
            productFilterInfo = ApplicationState.ProductFilterInfo(
                rangeSort = ApplicationState.ProductFilterInfo.RangeSort(true, from, to)
            )
        )
    }

    fun rangeSort(
        activity: LifecycleOwner,
        controller: UiProductListFragmentController,
        viewModel: ProductListViewModel,
        from: BigDecimal,
        to: BigDecimal
    ) {
        combine(
            viewModel.uiProductReducer.reduce(viewModel.store).map { uiProducts ->
                val rangeSortedProducts: MutableList<UiProduct> = mutableListOf()
                uiProducts.forEach { uiProduct ->
                    if (uiProduct.product.price in from..to) {
                        rangeSortedProducts.add(uiProduct)
                    }
                }
                return@map rangeSortedProducts.toList()
            },
            viewModel.store.stateFlow.map {
                it.productFilterInfo
            }
        ) { rangeFilteredProducts, filterInfo ->
            val uiFilters: Set<UiFilter> =
                filterInfo.filterCategory.filters.map { filter ->
                    UiFilter(
                        filter = filter,
                        isSelected = filterInfo.filterCategory.selectedFilter?.equals(filter) == true
                    )
                }.toSet()

            return@combine ProductListFragmentUiState.Success(
                products = rangeFilteredProducts,
                filters = uiFilters
            )
        }.distinctUntilChanged().asLiveData().observe(activity) {
            controller.setData(it)
        }
        viewModel.refreshProducts()

    }

    fun updateSortType(applicationState: ApplicationState, sortType: Int): ApplicationState {
        // if sort type is already selected -> set isActive as false
        // else -> set sortType
        val newSortType = if (applicationState.productFilterInfo.sortType.sortType == sortType) {
            null
        } else {
            sortType
        }
        return applicationState.copy(
            productFilterInfo = ApplicationState.ProductFilterInfo(
                sortType =  ApplicationState.ProductFilterInfo.SortType(newSortType)
            )
        )
    }
}
