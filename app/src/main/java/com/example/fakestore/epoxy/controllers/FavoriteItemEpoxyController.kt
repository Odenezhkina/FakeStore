package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.FavoriteItemEpoxyModel
import com.example.fakestore.managers.uimanager.MainNavigator
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.viewmodels.ProductListViewModel

interface OnFavProductListener {
    fun onFavoriteIconListener(productId: Int)
    fun onAddToCartClickListener(productId: Int)
    fun onFavItemClickListener(productId: Int)
}

class FavoriteItemEpoxyController(
    private val viewModel: ProductListViewModel,
    private val navController: NavController
) : TypedEpoxyController<List<UiProduct>>() {

    override fun buildModels(data: List<UiProduct>?) {
        if (data.isNullOrEmpty()) {
            // todo handle this state
            return
        }
        data.forEach {
            FavoriteItemEpoxyModel(
                it,
                object : OnFavProductListener{
                    override fun onFavoriteIconListener(productId: Int) {
                        viewModel.updateFavoriteSet(productId)
                    }

                    override fun onAddToCartClickListener(productId: Int) {
                        viewModel.updateCartProductsId(productId)
                    }

                    override fun onFavItemClickListener(productId: Int) {
                        MainNavigator.navigateToProductDetailsFragment(
                            navController = navController,
                            productId = productId,
                            actionId = R.id.action_favoriteFragment_to_productDetailsFragment
                        )
                    }

                }
            ).id(it.product.id).addTo(this)
        }
    }

}
