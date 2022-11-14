package com.example.fakestore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.ProductRepository
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import com.example.fakestore.redux.updaters.CartUpdater
import com.example.fakestore.redux.updaters.FavUpdater
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*

class BaseViewModel(
    val store: Store<ApplicationState>,
    private val favUpdater: FavUpdater,
    private val cartUpdater: CartUpdater,
    private val productRepository: ProductRepository
) : ViewModel() {

    init{
        refreshProducts()
    }

    private fun refreshProducts() = viewModelScope.launch {
        val products: List<Product> = productRepository.fetchAllProducts()
        // // todo move to filter
//        val filters: Set<Filter> = filterGenerator.generateFilters(products)
        store.update { applicationState ->
            // todo move to filter
//            val maxCost: BigDecimal = Collections.max(products.map { it.price })
            return@update applicationState.copy(
                products = products,
                // updating productFilterInfo here too because we want to
                // write filters too
                // todo move to filter
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

}

