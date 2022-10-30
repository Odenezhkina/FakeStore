package com.example.fakestore.epoxy.model

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import coil.load
import com.airbnb.epoxy.EpoxyModel
import com.example.fakestore.R
import com.example.fakestore.databinding.CartItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.epoxy.listeners.OnCartProductListener
import com.example.fakestore.managers.uimanager.MainUiManager.formatToPrice
import com.example.fakestore.managers.uimanager.MainUiManager.setFavoriteIcon
import com.example.fakestore.model.ui.CartUiProduct

class CartProductEpoxyModel(
    val cartProduct: CartUiProduct,
    private val listener: OnCartProductListener? = null
) :
    ViewBindingKotlinModel<CartItemBinding>(R.layout.cart_item) {

    override fun bind(view: View, previouslyBoundModel: EpoxyModel<*>) {
        Log.d("TAGTAG", "hello it is me!!!!")
        super.bind(view, previouslyBoundModel)
    }

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
            btnDelete.setOnClickListener {
                listener?.delOnClickListener(id)
            }
        }
        with(cartProduct) {
            tvQuantity.text = quantityInCart.toString()
            btnToFavorites.setFavoriteIcon(uiProduct.isInFavorites)
            btnMinus.setOnClickListener {
                listener?.quantityChangeListener(id, quantityInCart - 1)
            }
            btnPlus.setOnClickListener {
                listener?.quantityChangeListener(id, quantityInCart + 1)
            }
            root.setOnClickListener {
                listener?.onCardClickListener(id)
            }
        }

    }
}
