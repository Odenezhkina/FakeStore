package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.EmptyListEpoxyModel
import com.example.fakestore.epoxy.model.FavoriteItemEpoxyModel
import com.example.fakestore.managers.uimanager.navigateToProductDetailsFragment
import com.example.fakestore.states.FavFragmentUiState
import com.example.fakestore.viewmodels.ProductListViewModel

class FavoriteItemEpoxyController(
    private val viewModel: ProductListViewModel, private val navController: NavController
) : TypedEpoxyController<FavFragmentUiState>() {

    override fun buildModels(data: FavFragmentUiState?) {
        when (data) {
            null, is FavFragmentUiState.Empty -> EmptyListEpoxyModel(::goToCatalog).id(2)
                .addTo(this)
            is FavFragmentUiState.NonEmpty -> {
                data.products.forEach {
                    FavoriteItemEpoxyModel(it, object : GeneralProductClickListener {
                        override fun onFavClickListener(productId: Int) {
                            viewModel.updateFavoriteSet(productId)
                        }

                        override fun onToCartListener(productId: Int) {
                            viewModel.updateCartProductsId(productId)
                        }

                        override fun onCardClickListener(productId: Int) {
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
