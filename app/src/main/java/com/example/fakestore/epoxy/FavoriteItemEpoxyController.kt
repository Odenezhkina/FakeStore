package com.example.fakestore.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.epoxy.model.FavoriteItemEpoxyModel
import com.example.fakestore.model.ui.UiProduct


class FavoriteItemEpoxyController : TypedEpoxyController<List<UiProduct>>() {

    // todo maybe it is better to create another model FavProduct and use it instead of UiProduct

    override fun buildModels(data: List<UiProduct>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            FavoriteItemEpoxyModel(
                it
            ).id(it.product.id).addTo(this)
        }
    }

}
