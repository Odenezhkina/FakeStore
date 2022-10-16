package com.example.fakestore.managers.uimanager

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

object MainNavigator {

    fun navigateToProductDetailsFragment(navController: NavController, productId: Int, @IdRes actionId: Int) {
        navController.navigate(
            actionId,
            Bundle().apply { putInt(MainUiManager.KEY_PRODUCT_ID, productId) })
    }
}
