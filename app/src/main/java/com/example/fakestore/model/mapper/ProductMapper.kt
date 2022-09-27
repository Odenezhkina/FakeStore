package com.example.fakestore.model.mapper

import com.example.fakestore.model.domain.Product
import com.example.fakestore.model.network.NetworkProduct
import com.example.fakestore.model.network.NetworkRating
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

class ProductMapper @Inject constructor(){

    fun buildFrom(networkProduct: NetworkProduct): Product {
        return Product(
            capitalize(networkProduct.category),
            networkProduct.description,
            networkProduct.id,
            networkProduct.image,
            BigDecimal(networkProduct.price).setScale(2, RoundingMode.HALF_UP),
            NetworkRating(networkProduct.rating.count, networkProduct.rating.rate),
            networkProduct.title
        )
    }

    private fun capitalize(str: String):String{
        return str.replaceFirstChar {
            if(it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}
