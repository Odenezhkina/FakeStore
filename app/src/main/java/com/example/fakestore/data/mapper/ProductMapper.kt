package com.example.fakestore.data.mapper

import com.example.fakestore.domain.model.Product
import com.example.fakestore.data.model.NetworkProduct
import com.example.fakestore.data.model.NetworkRating
import com.example.fakestore.domain.ProductInfoGenerator
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

class ProductMapper @Inject constructor(
    private val productInfoGenerator: ProductInfoGenerator
) {

    fun buildFrom(networkProduct: NetworkProduct): Product {
        return Product(
            capitalize(networkProduct.category),
            networkProduct.description,
            networkProduct.id,
            networkProduct.image,
            BigDecimal(networkProduct.price).setScale(2, RoundingMode.HALF_UP),
            NetworkRating(networkProduct.rating.count, networkProduct.rating.rate),
            networkProduct.title,
            manufacturer = productInfoGenerator.generateManufacturer(),
            discount = productInfoGenerator.generateDiscount(),
            quantityOnWarehouse = productInfoGenerator.generateQuantity()
        )
    }

    private fun capitalize(str: String):String{
        return str.replaceFirstChar {
            if(it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}
