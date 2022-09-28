package com.example.fakestore.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.MainViewModel
import com.example.fakestore.epoxy.model.FavoriteItemEpoxyModel
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.uimanager.ProductListUiManager


class FavoriteItemEpoxyController(private val viewModel: MainViewModel) : TypedEpoxyController<List<UiProduct>>() {

    // todo maybe it is better to create another model FavProduct and use it instead of UiProduct

    override fun buildModels(data: List<UiProduct>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            FavoriteItemEpoxyModel(
                it, ::inFavoriteIconListener).id(it.product.id).addTo(this)
        }
    }

    private fun inFavoriteIconListener(favItemId: Int){
        ProductListUiManager.onFavoriteIconListener(favItemId, viewModel)
    }

}
