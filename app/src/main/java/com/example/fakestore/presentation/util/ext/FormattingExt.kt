package com.example.fakestore.presentation.util.ext

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

private const val CURRENCY = "USD"

fun BigDecimal.formatToPrice(): String {
    return NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(CURRENCY)
    }.format(this)
}
