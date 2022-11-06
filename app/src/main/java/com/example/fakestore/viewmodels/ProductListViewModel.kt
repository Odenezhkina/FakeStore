package com.example.fakestore.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.ProductRepository
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import com.example.fakestore.redux.generator.FilterGenerator
import com.example.fakestore.redux.reducer.UiProductReducer
import com.example.fakestore.redux.updaters.CartUpdater
import com.example.fakestore.redux.updaters.FavUpdater
import com.example.fakestore.redux.updaters.FilterUpdater
import com.example.fakestore.utils.SortManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Collections.max
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject constructor(
    // todo fix error
    val store: Store<ApplicationState>,
    private val productRepository: ProductRepository,
    val uiProductReducer: UiProductReducer,
    private val favUpdater: FavUpdater,
    private val filterUpdater: FilterUpdater,
    private val cartUpdater: CartUpdater,
    private val filterGenerator: FilterGenerator,
    private val sorter: SortManager // todo
) : ViewModel() {

    init {
        refreshProducts()
    }

    private fun refreshProducts() = viewModelScope.launch {
        val products: List<Product> = productRepository.fetchAllProducts()
        val filters: Set<Filter> = filterGenerator.generateFilters(products)
        store.update { applicationState ->

            val maxCost: BigDecimal = max(products.map { it.price })
            return@update applicationState.copy(
                products = products,
                // updating productFilterInfo here too because we want to
                // write filters too
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

    fun updateFavoriteSet(productId: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update favUpdater.update(applicationState, productId)
        }
    }

    fun updateCartProductsId(productId: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update cartUpdater.update(applicationState, productId)
        }
    }

    fun updateSelectedFilter(filter: Filter) = viewModelScope.launch {
        store.update { applicationState ->
            return@update filterUpdater.updateSelectedCategory(applicationState, filter)
        }
    }

    fun updateSortType(sortType: Int) = viewModelScope.launch {
        store.update { applicationState ->
            Log.d("TAGTAG", "$javaClass : updating sort type ")
            return@update filterUpdater.updateSortType(applicationState, sortType)
        }
    }

    fun updateRangeSort(newFromCost: BigDecimal, newToCost: BigDecimal) = viewModelScope.launch {
        store.update { applicationState ->
            return@update filterUpdater.updateRangeSort(applicationState, newFromCost, newToCost)
        }
    }
}
