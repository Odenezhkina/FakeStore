package com.example.fakestore.presentation.util.ext

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.fakestore.R
import com.google.android.material.button.MaterialButton

private const val IC_DONE = R.drawable.ic_round_done_24
private const val IC_CART = R.drawable.ic_baseline_add_shopping_cart_24
private const val IC_FAVORITE_ACTIVE = R.drawable.ic_round_favorite_24
private const val IC_FAVORITE = R.drawable.ic_round_favorite_border_24
private const val COLOR_ORANGE = R.color.orange
private const val COLOR_DARK_BLUE = R.color.dark_blue

fun MaterialButton.setInCartStyle(isInCart: Boolean, context: Context) {
    val colorId = if (isInCart) {
        this.setIconResource(IC_DONE)
        COLOR_ORANGE
    } else {
        this.setIconResource(IC_CART)
        COLOR_DARK_BLUE
    }
    this.setBackgroundColor(ContextCompat.getColor(context, colorId))
}

fun MaterialButton.setBtnToCartStyle(isInCart: Boolean, context: Context) {
    val colorId = if (isInCart) {
        this.text = context.getString(R.string.btn_buy_text_added)
        COLOR_ORANGE
    } else {
        this.text = context.getString(R.string.btn_buy_text_not_added)
        COLOR_DARK_BLUE
    }
    this.setBackgroundColor(ContextCompat.getColor(context, colorId))
}

fun MaterialButton.setFavoriteIcon(isInFavorites: Boolean) {
    setIconResource(
        if (isInFavorites) IC_FAVORITE_ACTIVE
        else IC_FAVORITE
    )
}
