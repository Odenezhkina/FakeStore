package com.example.fakestore.model.mapper

import com.example.fakestore.model.domain.Product
import com.example.fakestore.model.network.NetworkProduct
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ProductMapper @Inject constructor(){

    fun buildFrom(networkProduct: NetworkProduct): Product {
        return Product(
            networkProduct.category,
            networkProduct.description,
            networkProduct.id,
            networkProduct.image,
            BigDecimal(networkProduct.price).setScale(2, RoundingMode.HALF_UP),
            networkProduct.rating.rate,
            networkProduct.title
        )
    }
}
