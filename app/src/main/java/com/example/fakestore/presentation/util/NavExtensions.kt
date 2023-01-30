package com.example.fakestore.utils.uimanager

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.navigateToProductDetailsFragment(productId: Int, @IdRes actionId: Int) {
    this.navigate(
        actionId,
        Bundle().apply { putInt(MainUiManager.KEY_PRODUCT_ID, productId) })
}

