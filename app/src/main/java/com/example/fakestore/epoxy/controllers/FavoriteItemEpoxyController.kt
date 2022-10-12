package com.example.fakestore.epoxy.controllers

import android.content.res.Resources
import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.FavoriteItemEpoxyModel
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.managers.uimanager.MainNavigator
import com.example.fakestore.viewmodels.MainViewModel


class FavoriteItemEpoxyController(
    private val viewModel: MainViewModel,
    private val navController: NavController
) : TypedEpoxyController<List<UiProduct>>() {

    override fun buildModels(data: List<UiProduct>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            FavoriteItemEpoxyModel(
                it,
                ::onFavClickListener,
                ::onToCartClickListener,
                ::onFavItemClickListener
            ).id(it.product.id).addTo(this)
        }
    }

    private fun onFavClickListener(productId: Int) {
        viewModel.updateFavoriteSet(productId)
    }

    private fun onToCartClickListener(productId: Int) {
        viewModel.updateCartProductsId(productId)
    }


    private fun onFavItemClickListener(productId: Int) {
        MainNavigator.navigateToProductDetailsFragment(
            navController = navController,
            productId = productId,
            actionId = R.id.action_favoriteFragment_to_productDetailsFragment
        )
    }

}
