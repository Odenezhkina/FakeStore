package com.example.fakestore.uimanager

import android.os.Bundle
import androidx.navigation.NavController
import com.example.fakestore.MainViewModel
import com.example.fakestore.R

object ProductListUiManager {
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

    fun onProductClickListener(productId: Int, navController: NavController) {
        navController.navigate(
            R.id.action_productListFragment_to_productDetailsFragment,
            Bundle().apply { putInt(KEY_PRODUCT_ID, productId) })
    }

    fun onFavoriteIconListener(productId: Int, viewModel: MainViewModel) {
        viewModel.updateFavoriteSet(productId)
    }


}

