package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.UiFilterEpoxyModel
import com.example.fakestore.epoxy.model.UiProductEpoxyModel
import com.example.fakestore.managers.uimanager.navigateToProductDetailsFragment
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.viewmodels.ProductListViewModel
import java.util.*

interface OnUiProductListener{
    fun onFavoriteBtnChangeListener(productId: Int)
    fun onCardClickListener(productId: Int)
    fun onAddToCartClickListener(productId: Int)
}

class UiProductListFragmentController(
    private val viewModel: ProductListViewModel,
    private val navController: NavController
) : TypedEpoxyController<ProductListFragmentUiState>() {

    override fun buildModels(data: ProductListFragmentUiState?) {
        when (data) {
            is ProductListFragmentUiState.Loading -> {
                repeat(7) {
                    // should or not pass if product is null
                    UiProductEpoxyModel(
                        null).id(
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
                        object : OnUiProductListener{
                            override fun onFavoriteBtnChangeListener(productId: Int) {
                                viewModel.updateFavoriteSet(productId)
                            }

                            override fun onCardClickListener(productId: Int) {
                                navController.navigateToProductDetailsFragment(
                                    productId = productId,
                                    actionId = R.id.action_productListFragment_to_productDetailsFragment
                                )
                            }

                            override fun onAddToCartClickListener(productId: Int) {
                                viewModel.updateCartProductsId(productId)
                            }

                        }
                    ).id(it.product.id).addTo(this)
                }
            }
            else -> {
                // todo throw some error do smth else
            }
        }
    }

    private fun onFilterClickListener(filter: Filter) {
        viewModel.updateSelectedFilter(filter)
    }
}
