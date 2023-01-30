package com.example.fakestore.presentation.favorite

import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.presentation.util.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.presentation.util.EmptyListEpoxyModel
import com.example.fakestore.utils.uimanager.navigateToProductDetailsFragment
import com.example.fakestore.presentation.catalog.ProductListViewModel

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

                    }).id(it.toString()).addTo(this)
                }
            }
        }

    }

    private fun goToCatalog() {
        navController.navigate(R.id.action_favoriteFragment_to_productListFragment)
    }
}
