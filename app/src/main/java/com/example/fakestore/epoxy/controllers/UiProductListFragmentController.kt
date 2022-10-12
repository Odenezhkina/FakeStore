package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.UiFilterEpoxyModel
import com.example.fakestore.epoxy.model.UiProductEpoxyModel
import com.example.fakestore.managers.uimanager.MainNavigator
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.viewmodels.MainViewModel
import java.util.*

class UiProductListFragmentController(
    private val viewModel: MainViewModel,
    private val navController: NavController
) : TypedEpoxyController<ProductListFragmentUiState>() {

    override fun buildModels(data: ProductListFragmentUiState?) {
        when (data) {
            is ProductListFragmentUiState.Loading -> {
                repeat(7) {
                    // should or not pass if product is null
                    UiProductEpoxyModel(
                        null,
                        ::onFavoriteBtnChangeListener,
                        ::onCardClickListener,
                        ::onAddToCartClickListener
                    ).id(
                        // todo check if uuid is everywhere
                        UUID.randomUUID().toString()
                    ).addTo(this)
                }
                return
            }
            is ProductListFragmentUiState.Success -> {
                val uiFilterModels = data.filters.map { uifilter ->
                    UiFilterEpoxyModel(
                        uiFilter = uifilter,
                        onFilterClickListener = ::onFilterClickListener).id(uifilter.filter.title)
                }
                CarouselModel_().models(uiFilterModels).id("").addTo(this)

                data.products.forEach {
                    UiProductEpoxyModel(
                        it,
                        ::onFavoriteBtnChangeListener,
                        ::onCardClickListener,
                        ::onAddToCartClickListener
                    ).id(it.product.id).addTo(this)
                }
            }
            else -> {
                // todo throw some error do smth else
            }
        }
    }

    private fun onFavoriteBtnChangeListener(productId: Int) {
        viewModel.updateFavoriteSet(productId)
    }

    private fun onCardClickListener(productId: Int) {
        MainNavigator.navigateToProductDetailsFragment(
            navController = navController,
            productId =  productId,
            actionId = R.id.action_productListFragment_to_productDetailsFragment
        )
    }

    private fun onFilterClickListener(filter: Filter) {
        viewModel.updateSelectedFilter(filter)
    }

    private fun onAddToCartClickListener(productId: Int) {
        viewModel.updateCartProductsId(productId)
    }
}
