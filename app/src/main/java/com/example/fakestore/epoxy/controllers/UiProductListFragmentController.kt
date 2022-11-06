package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.epoxy.model.UiProductEpoxyModel
import com.example.fakestore.utils.uimanager.navigateToProductDetailsFragment
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.states.ProductListFragmentUiState
import com.example.fakestore.viewmodels.ProductListViewModel
import java.util.*


class UiProductListFragmentController(
    private val viewModel: ProductListViewModel,
    private val navController: NavController
) : TypedEpoxyController<ProductListFragmentUiState>() {

    override fun buildModels(data: ProductListFragmentUiState?) {
        when (data) {
            is ProductListFragmentUiState.Loading -> {
                UiProductEpoxyModel(null).id(UUID.randomUUID().toString()).addTo(this)
                return
            }
            is ProductListFragmentUiState.Success -> {
//                val uiFilterModels = data.filters.map { uifilter ->
//                    UiFilterEpoxyModel(
//                        uiFilter = uifilter,
//                        onFilterClickListener = ::onFilterClickListener).id(uifilter.filter.title)
//                }
//                CarouselModel_().models(uiFilterModels).id("").addTo(this)

                data.products.forEach {
                    UiProductEpoxyModel(
                        it,
                        object : GeneralProductClickListener {
                            override fun onFavClickListener(productId: Int) {
                                viewModel.updateFavoriteSet(productId)
                            }

                            override fun onToCartListener(productId: Int) {
                                viewModel.updateCartProductsId(productId)
                            }

                            override fun onCardClickListener(productId: Int) {
                                navController.navigateToProductDetailsFragment(
                                    productId = productId,
                                    actionId = R.id.action_productListFragment_to_productDetailsFragment
                                )
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
