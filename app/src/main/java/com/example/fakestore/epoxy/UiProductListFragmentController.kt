package com.example.fakestore.epoxy

import android.content.res.Resources
import androidx.navigation.NavController
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.MainViewModel
import com.example.fakestore.epoxy.model.UiFilterEpoxyModel
import com.example.fakestore.epoxy.model.UiProductEpoxyModel
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.uimanager.ProductListUiManager

class UiProductListFragmentController(
    val res: Resources,
    private val viewModel: MainViewModel,
    private val navController: NavController
) : TypedEpoxyController<ProductListFragmentUiState>() {

    override fun buildModels(data: ProductListFragmentUiState?) {
        if (data == null) {
            repeat(7) {
                val epoxyId = it + 1
                // should or not pass if product is null
                UiProductEpoxyModel(null, ::onFavoriteBtnChangeListener, ::onCardClickListener).id(
                    epoxyId
                ).addTo(this)
            }
            return
        }

        // setting filters in carousel
        val uiFilterModels = data.filters.map { uifilter ->
            UiFilterEpoxyModel(
                uiFilter = uifilter,
                onFilterClickListener = ::onFilterClickListener
            ).id(uifilter.filter.title)
        }
        CarouselModel_().models(uiFilterModels).id("").addTo(this)

        data.products.forEach {
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
        ProductListUiManager.onFavoriteIconListener(productId, viewModel)
    }

    private fun onCardClickListener(productId: Int) {
       ProductListUiManager.onProductClickListener(productId, navController)
    }

    private fun onFilterClickListener(filter: Filter) {
        viewModel.updateSelectedFilter(filter)
    }
}
