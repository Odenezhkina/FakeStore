package com.example.fakestore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.ProductRepository
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import com.example.fakestore.redux.reducer.UiProductReducer
import com.example.fakestore.redux.updaters.CartUpdater
import com.example.fakestore.redux.updaters.FavUpdater
import com.example.fakestore.redux.updaters.FilterUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    val store: Store<ApplicationState>,
    private val productRepository: ProductRepository,
    val uiProductReducer: UiProductReducer,
    private val favUpdater: FavUpdater,
    private val filterUpdater: FilterUpdater,
    private val cartUpdater: CartUpdater,
//    val uiProductDetailedReducer: UiProductDetailedReducer
) : ViewModel() {
    // todo divide maybe in two or more view models

    fun refreshProducts() = viewModelScope.launch {
        val products: List<Product> = productRepository.fetchAllProducts()
        store.update { applicationState ->
            return@update applicationState.copy(
                products = products,
                // updating productFilterInfo here too because we want to
                // write filters too
                productFilterInfo = ApplicationState.ProductFilterInfo(
                    filters = products.map { product ->
                        Filter(title = product.category, displayedTitle = product.category)
                    }.toSet(), selectedFilter = applicationState.productFilterInfo.selectedFilter
                )
            )
        }
    }

    fun updateFavoriteSet(productId: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update favUpdater.update(applicationState, productId)
        }
    }

    fun updateSelectedFilter(filter: Filter) = viewModelScope.launch {
        store.update { applicationState ->
            return@update filterUpdater.update(applicationState, filter)
        }
    }

    fun updateCartProductsId(productId: Int) = viewModelScope.launch {
        store.update { applicationState ->
            return@update cartUpdater.update(applicationState, productId)
        }
    }
}
