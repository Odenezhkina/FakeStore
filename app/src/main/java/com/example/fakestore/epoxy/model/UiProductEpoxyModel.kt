package com.example.fakestore.epoxy.model

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.epoxy.controllers.OnUiProductListener
import com.example.fakestore.managers.uimanager.MainUiManager.formatToPrice
import com.example.fakestore.managers.uimanager.MainUiManager.setFavoriteIcon
import com.example.fakestore.managers.uimanager.MainUiManager.setInCartStyle
import com.example.fakestore.model.ui.UiProduct

data class UiProductEpoxyModel(
    private val product: UiProduct?,
    private val listener: OnUiProductListener? = null
) : ViewBindingKotlinModel<ProductItemBinding>(R.layout.product_item) {

    override fun ProductItemBinding.bind() {
        // product not null
        shimmerLayout.isVisible = product == null
        cardview.isInvisible = product == null

        product?.run {
            shimmerLayout.stopShimmer()

            tvHeadline.text = product.title
            tvCategory.text = product.category
            tvPrice.text = product.price.formatToPrice()

            btnToFavorites.setFavoriteIcon(isInFavorites)

            ratingBar.rating = product.rating.rate

            pbLoadingImage.isVisible = true
            ivImage.load(data = product.image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }

            btnToCart.setInCartStyle(isInCart, root.context)

            // listeners
            btnToFavorites.setOnClickListener {
                listener?.onFavoriteBtnChangeListener(productId = product.id)
            }

            btnToCart.setOnClickListener {
                listener?.onAddToCartClickListener(productId = product.id)
            }

            cardview.setOnClickListener {
                listener?.onCardClickListener(productId = product.id)
            }

        } ?: shimmerLayout.startShimmer()
    }
}
