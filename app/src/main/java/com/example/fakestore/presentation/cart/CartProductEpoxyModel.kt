package com.example.fakestore.presentation.cart

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.CartItemBinding
import com.example.fakestore.presentation.util.ext.formatToPrice
import com.example.fakestore.presentation.model.CartUiProduct
import com.example.fakestore.presentation.util.ViewBindingKotlinModel
import com.example.fakestore.presentation.util.epoxy.listeners.OnCartProductListener
import com.example.fakestore.presentation.util.ext.setFavoriteIcon


class CartProductEpoxyModel(
    val cartProduct: CartUiProduct,
    private val listener: OnCartProductListener? = null
) :
    ViewBindingKotlinModel<CartItemBinding>(R.layout.cart_item) {

    override fun CartItemBinding.bind() {
        val id = cartProduct.uiProduct.product.id
        cartProduct.uiProduct.product.run {
            tvHeadline.text = title
            tvPrice.text = price.formatToPrice()

            ivImage.load(image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }
            ratingBar.rating = rating.rate

            btnToFavorites.setOnClickListener {
                listener?.onFavClickListener(id)
            }
        }
        with(cartProduct) {
            btnQuantity.tvQuantity.text = quantityInCart.toString()
            btnToFavorites.setFavoriteIcon(uiProduct.isInFavorites)
            btnQuantity.btnMinus.setOnClickListener {
                listener?.quantityChangeListener(id, quantityInCart - 1)
            }
            btnQuantity.btnPlus.setOnClickListener {
                listener?.quantityChangeListener(id, quantityInCart + 1)
            }
            root.setOnClickListener {
                listener?.onCardClickListener(id)
            }
        }

    }
}
