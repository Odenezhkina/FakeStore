package com.example.fakestore.epoxy.controllers

import android.content.res.Resources
import android.os.Bundle
import androidx.navigation.NavController
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.UiFilterEpoxyModel
import com.example.fakestore.epoxy.model.UiProductEpoxyModel
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.uimanager.MainUiManager
import com.example.fakestore.viewmodels.MainViewModel
import java.util.UUID

class UiProductListFragmentController(
    val res: Resources,
    private val viewModel: MainViewModel,
    private val navController: NavController
) : TypedEpoxyController<ProductListFragmentUiState>() {

    override fun buildModels(data: ProductListFragmentUiState?) {
        if (data == null) {
            repeat(7) {
                // should or not pass if product is null
                UiProductEpoxyModel(
                    res,
                    null,
                    ::onFavoriteBtnChangeListener,
                    ::onCardClickListener,
                    ::onAddToCartClickListener
                ).id(
                    // todo check if uuid is everywhere
                    UUID.randomUUID().toString()
                ).addTo(this)
            }
            return
        }
        // setting filters in carousel
        val uiFilterModels = data.filters.map { uifilter ->
            UiFilterEpoxyModel(
                res,
                uiFilter = uifilter,
                onFilterClickListener = ::onFilterClickListener
            ).id(uifilter.filter.title)
        }
        CarouselModel_().models(uiFilterModels).id("").addTo(this)

        data.products.forEach {
            UiProductEpoxyModel(
                res,
                it,
                ::onFavoriteBtnChangeListener,
                ::onCardClickListener,
                ::onAddToCartClickListener
            ).id(it.product.id).addTo(this)
        }
    }

    private fun onFavoriteBtnChangeListener(productId: Int) {
        // change icon(solid favorite) + change color
        // save changed state
        viewModel.updateFavoriteSet(productId)
    }

    private fun onCardClickListener(productId: Int) {
        navController.navigate(
            R.id.action_productListFragment_to_productDetailsFragment,
            Bundle().apply { putInt(MainUiManager.KEY_PRODUCT_ID, productId) })
    }

    private fun onFilterClickListener(filter: Filter) {
        viewModel.updateSelectedFilter(filter)
    }

    private fun onAddToCartClickListener(productId: Int) {
        viewModel.updateCartProductsId(productId)
    }
}
