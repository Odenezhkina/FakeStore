package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.model.CartProductEpoxyModel
import com.example.fakestore.managers.uimanager.navigateToProductDetailsFragment
import com.example.fakestore.model.ui.CartUiProduct
import com.example.fakestore.viewmodels.CartViewModel

interface OnCardProductListener{
    fun onFavClickListener(productId: Int)
    fun delOnClickListener(productId: Int)
    fun quantityChangeListener(productId: Int, updatedQuantity: Int)
    fun onCardProductItemListener(productId: Int)
}

class CartProductEpoxyController(private val viewModel: CartViewModel, private val navController: NavController) :
    TypedEpoxyController<List<CartUiProduct>>() {

    override fun buildModels(data: List<CartUiProduct>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            CartProductEpoxyModel(
                cartProduct = it,
                object : OnCardProductListener{
                    override fun onFavClickListener(productId: Int) {
                        viewModel.updateFavoriteSet(productId)
                    }

                    override fun delOnClickListener(productId: Int) {
                        viewModel.updateCartProductsId(productId)
                    }

                    override fun quantityChangeListener(productId: Int, updatedQuantity: Int) {
                        viewModel.updateCartQuantity(productId, updatedQuantity)
                    }

                    override fun onCardProductItemListener(productId: Int) {
                        navController.navigateToProductDetailsFragment(
                            productId,
                            R.id.action_cartFragment_to_productDetailsFragment)
                    }

                }
            ).id(it.uiProduct.product.id).addTo(this)
        }
    }

}

