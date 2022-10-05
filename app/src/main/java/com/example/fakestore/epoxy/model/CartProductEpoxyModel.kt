package com.example.fakestore.epoxy.model

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.CartItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.model.ui.CartUiProduct
import com.example.fakestore.uimanager.MainUiManager

class CartProductEpoxyModel(
    private val cartProduct: CartUiProduct,
    private val favOnClickListener: (Int) -> Unit,
    private val delOnClickListener: (Int) -> Unit,
    private val quantityChangeListener: (Int, Int) -> Unit
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
            btnToFavorites.setOnClickListener {
                favOnClickListener(uiProduct.product.id)
            }
            btnDelete.setOnClickListener {
                delOnClickListener(uiProduct.product.id)
            }
            btnMinus.setOnClickListener {
                quantityChangeListener(uiProduct.product.id, quantityInCart - 1)
            }
            btnPlus.setOnClickListener {
                quantityChangeListener(uiProduct.product.id, quantityInCart + 1)
            }
        }
    }
}
