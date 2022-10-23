package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.EmptyListEpoxyModel
import com.example.fakestore.epoxy.model.FavoriteItemEpoxyModel
import com.example.fakestore.managers.uimanager.navigateToProductDetailsFragment
import com.example.fakestore.states.FavFragmentUiState
import com.example.fakestore.viewmodels.ProductListViewModel

interface OnFavProductListener {
    fun onFavoriteIconListener(productId: Int)
    fun onAddToCartClickListener(productId: Int)
    fun onFavItemClickListener(productId: Int)
}

class FavoriteItemEpoxyController(
    private val viewModel: ProductListViewModel, private val navController: NavController
) : TypedEpoxyController<FavFragmentUiState>() {

    override fun buildModels(data: FavFragmentUiState?) {
        when (data) {
            null, is FavFragmentUiState.Empty -> EmptyListEpoxyModel(::goToCatalog).id(1)
                .addTo(this)
            is FavFragmentUiState.NonEmpty -> {
                data.products.forEach {
                    FavoriteItemEpoxyModel(it, object : OnFavProductListener {
                        override fun onFavoriteIconListener(productId: Int) {
                            viewModel.updateFavoriteSet(productId)
                        }

                        override fun onAddToCartClickListener(productId: Int) {
                            viewModel.updateCartProductsId(productId)
                        }

                        override fun onFavItemClickListener(productId: Int) {
                            navController.navigateToProductDetailsFragment(
                                productId = productId,
                                actionId = R.id.action_favoriteFragment_to_productDetailsFragment
                            )
                        }

                    }).id(it.product.id).addTo(this)
                }
            }
        }

    }

    private fun goToCatalog() {
        navController.navigate(R.id.action_favoriteFragment_to_productListFragment)
    }
}
