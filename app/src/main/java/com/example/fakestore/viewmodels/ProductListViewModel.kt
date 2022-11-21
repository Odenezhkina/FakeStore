package com.example.fakestore.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import com.example.fakestore.redux.generator.FilterGenerator
import com.example.fakestore.redux.reducer.CartProductReducer
import com.example.fakestore.redux.reducer.UiProductReducer
import com.example.fakestore.redux.updaters.CartUpdater
import com.example.fakestore.redux.updaters.FavUpdater
import com.example.fakestore.redux.updaters.FilterUpdater
import com.example.fakestore.repository.ProductRepository
import com.example.fakestore.utils.SortManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Collections.max
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject constructor(
//    store: Store<ApplicationState>,
//    productRepository: ProductRepository,
//    cartUpdater: CartUpdater,
//    favUpdater: FavUpdater,
    val uiProductReducer: UiProductReducer,
    private val filterUpdater: FilterUpdater,
    val uiProductDetailedReducer: CartProductReducer,
    private val filterGenerator: FilterGenerator,
    private val sorter: SortManager // todo
) : BaseViewModel() {

    init {
//        refreshProducts()
//        loadFilters()
    }
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


//    private fun refreshProducts() = viewModelScope.launch {
//        val products: List<Product> = productRepository.fetchAllProducts()
//        val filters: Set<Filter> = filterGenerator.generateFilters(products)
//        store.update { applicationState ->
//
//            val maxCost: BigDecimal = max(products.map { it.price })
//            return@update applicationState.copy(
//                products = products,
//                // updating productFilterInfo here too because we want to
//                // write filters too
//                productFilterInfo = ApplicationState.ProductFilterInfo(
//                    filterCategory = ApplicationState.ProductFilterInfo.FilterCategory(
//                        filters = filters,
//                        selectedFilter = applicationState.productFilterInfo.filterCategory.selectedFilter
//                    ),
//                    rangeSort = ApplicationState.ProductFilterInfo.RangeSort(
//                        fromCost = 0.toBigDecimal(),
//                        toCost = maxCost
//                    )
//                )
//            )
//        }
//    }

//    fun updateFavoriteSet(productId: Int) = viewModelScope.launch {
//        store.update { applicationState ->
//            return@update favUpdater.update(applicationState, productId)
//        }
//    }
//
//    fun updateCartProductsId(productId: Int) = viewModelScope.launch {
//        store.update { applicationState ->
//            return@update cartUpdater.update(applicationState, productId)
//        }
//    }

    fun updateSelectedFilter(filter: Filter) = viewModelScope.launch {
        store.update { applicationState ->
            return@update filterUpdater.updateSelectedCategory(applicationState, filter)
        }
    }

    fun updateSortType(sortType: Int) = viewModelScope.launch {
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
