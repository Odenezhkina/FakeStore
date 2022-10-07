package com.example.fakestore.epoxy.controllers

import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.epoxy.model.CartProductEpoxyModel
import com.example.fakestore.model.ui.CartUiProduct
import com.example.fakestore.viewmodels.CartViewModel

class CartProductEpoxyController(private val viewModel: CartViewModel) :
    TypedEpoxyController<List<CartUiProduct>>() {

    override fun buildModels(data: List<CartUiProduct>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            CartProductEpoxyModel(
                cartProduct = it,
                ::onFavClickListener,
                ::delOnClickListener,
                ::quantityChangeListener
            ).id(it.uiProduct.product.id).addTo(this)
        }
    }

    private fun onFavClickListener(productId: Int) {
        viewModel.updateFavoriteSet(productId)
    }

    private fun delOnClickListener(productId: Int) {
        // todo check if this works correct
        viewModel.updateCartProductsId(productId)
    }

    private fun quantityChangeListener(productId: Int, updatedQuantity: Int) {
        // todo check if this is ok or not
        viewModel.updateCartQuantity(productId, updatedQuantity)
    }

}

