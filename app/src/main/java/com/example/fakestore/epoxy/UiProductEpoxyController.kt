package com.example.fakestore.epoxy

import android.content.res.Resources
import android.os.Bundle
import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.MainActivity
import com.example.fakestore.MainViewModel
import com.example.fakestore.R
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.uicontrollers.UiController

class UiProductEpoxyController(
    val res: Resources,
    private val viewModel: MainViewModel,
    private val navController: NavController
) : TypedEpoxyController<List<UiProduct>>() {

    override fun buildModels(data: List<UiProduct>?) {
        // if data is null or empty send empty
        if (data.isNullOrEmpty()) {
            repeat(7) {
                val epoxyId = it + 1
                // should or not pass if product is null
                UiProductEpoxyModel(null, ::onFavoriteBtnChangeListener, ::onCardClickListener).id(
                    epoxyId
                ).addTo(this)
            }
            return
        }
        data.forEach {
            UiProductEpoxyModel(
                it,
                ::onFavoriteBtnChangeListener,
                ::onCardClickListener
            ).id(it.product.id).addTo(this)
        }
    }

    private fun onFavoriteBtnChangeListener(productId: Int) {
        // change icon(solid favorite) + change color
        // save changed state
//        viewModel.updateFavoriteSet(productId)
        UiController.onFavoriteClick(viewModel, productId)
    }

    //should we use nav controller
    private fun onCardClickListener(productId: Int) {
        navController.navigate(
            R.id.action_productListFragment_to_productDetailsFragment,
            Bundle().apply { putInt(MainActivity.KEY_PRODUCT_ID, productId) })
    }
}
