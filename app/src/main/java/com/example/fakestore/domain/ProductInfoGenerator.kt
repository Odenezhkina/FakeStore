package com.example.fakestore.domain

import kotlin.math.roundToInt

class ProductInfoGenerator {
    private val manufacturesArray = arrayOf(
        "Botanica",
        "Comfy",
        "Bottic",
        "Kotanic",
        "Cheerfy",
        "R'ostin",
        "Wucci",
        "Rokolov",
        "Benotfree",
        "Tuma",
        "Piiinko",
        "Zari",
        "Factomi",
        "Azany",
        "Lomiti",
        "Lame",
        "Hourse",
    )

    fun generateDiscount(): Int =
        (Math.random() * DISCOUNT_UPPER_BOUND).roundToInt()

    fun generateManufacturer(): String =
        manufacturesArray[(Math.random() * (manufacturesArray.size - 1)).roundToInt()]

    fun generateQuantity(): Int =
        (Math.random() * QUANTITY_UPPER_BOUND + QUANTITY_LOWER_BOUND).roundToInt()

    companion object {
        private const val DISCOUNT_UPPER_BOUND = 60
        private const val QUANTITY_UPPER_BOUND = 1000
        private const val QUANTITY_LOWER_BOUND = 5
    }
}
