package com.example.fakestore.managers.uimanager

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.fakestore.R
import com.google.android.material.button.MaterialButton
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

object MainUiManager {
    // object because all fun is static
    const val KEY_PRODUCT_ID = "product-id"

    fun MaterialButton.setFavoriteIcon(isInFavorites: Boolean) {
        this.setIconResource(
            if (isInFavorites) R.drawable.ic_round_favorite_24
            else R.drawable.ic_round_favorite_border_24
        )
    }

    fun getFilterBackgroundColorId(isSelected: Boolean): Int {
        return if (isSelected) R.color.orange else R.color.dark_blue
    }

    fun MaterialButton.setInCartStyle(isInCart: Boolean, context: Context) {
        val colorId = if (isInCart) {
            this.setIconResource(R.drawable.ic_round_done_24)
            R.color.orange
        } else {
            this.setIconResource(R.drawable.ic_baseline_add_shopping_cart_24)
            R.color.dark_blue
        }
        this.setBackgroundColor(ContextCompat.getColor(context, colorId))
    }

    fun MaterialButton.setBtnToCartStyle(isInCart: Boolean, context: Context) {
        val colorId = if (isInCart) {
            this.text = context.getString(R.string.btn_buy_text_added)
            R.color.orange
        } else {
            this.text = context.getString(R.string.btn_buy_text_not_added)
            R.color.dark_blue
        }
        this.setBackgroundColor(ContextCompat.getColor(context, colorId))
    }


    fun BigDecimal.formatToPrice(): String {
        return NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance("USD")
        }.format(this)
    }
}

