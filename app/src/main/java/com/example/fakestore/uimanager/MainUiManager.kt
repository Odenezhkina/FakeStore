package com.example.fakestore.uimanager

import com.example.fakestore.R

object MainUiManager {
    // object because all fun is static
    const val KEY_PRODUCT_ID = "product-id"

    fun getResFavoriteIconId(isInFavorites: Boolean): Int {
        return if (isInFavorites) {
            R.drawable.ic_round_favorite_24
        } else {
            R.drawable.ic_round_favorite_border_24
        }
    }

    fun getFilterBackgroundColorId(isSelected: Boolean): Int {
        return if (isSelected) R.color.orange else R.color.dark_blue
    }

    fun getCartUi(isInCart: Boolean): Pair<Int, Int>{
        // background color to icon id
        return if(isInCart){
            R.color.orange to R.drawable.ic_round_done_24
        } else{
            R.color.dark_blue to R.drawable.ic_baseline_add_shopping_cart_24
        }

    }
}

