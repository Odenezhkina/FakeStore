package com.example.fakestore.epoxy

import android.content.res.Resources
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.MainViewModel
import com.example.fakestore.model.ui.UiProduct

class UiProductEpoxyController(val res: Resources, private val viewModel: MainViewModel) : TypedEpoxyController<List<UiProduct>>() {

    override fun buildModels(data: List<UiProduct>?) {
        // if data is null or empty send empty
        if (data.isNullOrEmpty()) {
            repeat(7) {
                val epoxyId = it + 1
                // should or not pass if product is null
                UiProductEpoxyModel(null, ::onFavoriteBtnChangeListener).id(epoxyId).addTo(this)
            }
            return
        }
        data.forEach {
            UiProductEpoxyModel(it, ::onFavoriteBtnChangeListener).id(it.product.id).addTo(this)
        }
    }

    private fun onFavoriteBtnChangeListener(productId: Int){
        // change icon(solid favorite) + change color
        // save changed state
        viewModel.updateFavoriteSet(productId)
    }
}
