package com.example.fakestore.epoxy.controllers

import android.content.res.Resources
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.epoxy.model.FavoriteItemEpoxyModel
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.viewmodels.MainViewModel


class FavoriteItemEpoxyController(
    private val res: Resources,
    private val viewModel: MainViewModel
) : TypedEpoxyController<List<UiProduct>>() {

    // todo maybe it is better to create another model FavProduct and use it instead of UiProduct

    override fun buildModels(data: List<UiProduct>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            FavoriteItemEpoxyModel(
                res,
                it,
                ::inFavoriteIconListener,
                ::onAddToCartClickListener
            ).id(it.product.id).addTo(this)
        }
    }

    private fun inFavoriteIconListener(productId: Int) {
        viewModel.updateFavoriteSet(productId)
    }

    private fun onAddToCartClickListener(productId: Int) {
        viewModel.updateCartProductsId(productId)
    }

}
