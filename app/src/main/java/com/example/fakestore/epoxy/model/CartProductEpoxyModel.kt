package com.example.fakestore.epoxy.model

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.CartItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.epoxy.controllers.OnCardProductlistener
import com.example.fakestore.managers.uimanager.MainUiManager
import com.example.fakestore.model.ui.CartUiProduct

class CartProductEpoxyModel(
    private val cartProduct: CartUiProduct,
    private val listener: OnCardProductlistener? = null
) :
    ViewBindingKotlinModel<CartItemBinding>(R.layout.cart_item) {

    override fun CartItemBinding.bind() {
        cartProduct.uiProduct.product.run {
            tvHeadline.text = title
            tvPrice.text = MainUiManager.formatPrice(price)

            ivImage.load(image){
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }
        }

        cartProduct.run {
            tvQuantity.text = quantityInCart.toString()
            btnToFavorites.setIconResource(MainUiManager.getResFavoriteIconId(uiProduct.isInFavorites))

            uiProduct.product.run {
                btnToFavorites.setOnClickListener {
                    listener?.onFavClickListener(id)
                }
                btnDelete.setOnClickListener {
                    listener?.delOnClickListener(id)
                }
                btnMinus.setOnClickListener {
                    listener?.quantityChangeListener(id, quantityInCart - 1)
                }
                btnPlus.setOnClickListener {
                    listener?.quantityChangeListener(id, quantityInCart + 1)
                }
            }
        }
    }
}
