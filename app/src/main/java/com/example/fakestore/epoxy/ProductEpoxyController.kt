package com.example.fakestore.epoxy

import android.content.res.Resources
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.model.domain.Product

class ProductEpoxyController(val res: Resources) : TypedEpoxyController<List<Product>>() {

    override fun buildModels(data: List<Product>?) {
        data?.let {
            for (product in it) {
                ProductEpoxyModel(product).id(product.id).addTo(this)
            }
        }
    }
}
