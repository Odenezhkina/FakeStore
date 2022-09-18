package com.example.fakestore.epoxy

import android.content.res.Resources
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.model.ui.UiProduct

class ProductEpoxyController(val res: Resources) : TypedEpoxyController<List<UiProduct>>() {

    override fun buildModels(data: List<UiProduct>?) {
        // if data is null or empty send empty
        if (data.isNullOrEmpty()) {
            repeat(7) {
                val epoxyId = it + 1
                ProductEpoxyModel(null).id(epoxyId).addTo(this)
            }
            return
        }
        data.forEach {
            ProductEpoxyModel(it).id(it.product.id).addTo(this)
        }
    }
}
