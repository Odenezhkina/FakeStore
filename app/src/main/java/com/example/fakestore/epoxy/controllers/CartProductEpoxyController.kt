package com.example.fakestore.epoxy.controllers

import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.epoxy.model.CartProductEpoxyModel
import com.example.fakestore.model.ui.CartUiProduct
import com.example.fakestore.viewmodels.CartViewModel

interface OnCardProductlistener{
    fun onFavClickListener(productId: Int)
    fun delOnClickListener(productId: Int)
    fun quantityChangeListener(productId: Int, updatedQuantity: Int)
}

class CartProductEpoxyController(private val viewModel: CartViewModel) :
    TypedEpoxyController<List<CartUiProduct>>() {

    override fun buildModels(data: List<CartUiProduct>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            CartProductEpoxyModel(
                cartProduct = it,
                object : OnCardProductlistener{
                    override fun onFavClickListener(productId: Int) {
                        viewModel.updateFavoriteSet(productId)
                    }

                    override fun delOnClickListener(productId: Int) {
                        viewModel.updateCartProductsId(productId)
                    }

                    override fun quantityChangeListener(productId: Int, updatedQuantity: Int) {
                        viewModel.updateCartQuantity(productId, updatedQuantity)
                    }

                }
            ).id(it.uiProduct.product.id).addTo(this)
        }
    }

}

