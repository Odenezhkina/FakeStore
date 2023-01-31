package com.example.fakestore.presentation.util.ext

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.example.fakestore.R

const val KEY_PRODUCT_ID = "product-id"

fun NavController.navigateToProductDetailsFragment(productId: Int, @IdRes actionId: Int) {
    this.navigate(
        actionId,
        Bundle().apply { putInt(KEY_PRODUCT_ID, productId) })
}


fun NavController.fromCartToCalogFragment() {
    navigate(R.id.action_cartFragment_to_productListFragment)
}
