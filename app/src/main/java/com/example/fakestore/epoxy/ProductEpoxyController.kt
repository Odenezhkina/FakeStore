package com.example.fakestore.epoxy

import android.content.res.Resources
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.model.domain.Product

class ProductEpoxyController(val res: Resources) : TypedEpoxyController<List<Product>>() {

    override fun buildModels(data: List<Product>?) {
        // if data is null or empty send empty
        if(data.isNullOrEmpty()){
            repeat(7){
                val epoxyId = it +1
                // id??
                ProductEpoxyModel(null).id(1).addTo(this)
            }
            return
        }
        data.forEach {
            ProductEpoxyModel(it).id(it.id).addTo(this)
        }
    }
}
