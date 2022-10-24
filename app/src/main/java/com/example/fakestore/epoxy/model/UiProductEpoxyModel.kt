package com.example.fakestore.epoxy.model

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.epoxy.controllers.GeneralProductClickListener
import com.example.fakestore.managers.uimanager.MainUiManager.formatToPrice
import com.example.fakestore.managers.uimanager.MainUiManager.setFavoriteIcon
import com.example.fakestore.managers.uimanager.MainUiManager.setInCartStyle
import com.example.fakestore.model.ui.UiProduct

data class UiProductEpoxyModel(
    private val product: UiProduct?,
    private val listener: GeneralProductClickListener? = null
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
            tvCountOfReviews.text =
                this@bind.root.context.getString(R.string.count_of_reviews, product.rating.count)

            pbLoadingImage.isVisible = true
            ivImage.load(data = product.image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }

            btnToCart.setInCartStyle(isInCart, root.context)

            btnToFavorites.setOnClickListener {
                listener?.onFavClickListener(product.id)
            }

            btnToCart.setOnClickListener {
                listener?.onToCartListener(product.id)
            }

            cardview.setOnClickListener {
                listener?.onCardClickListener(product.id)
            }

        } ?: shimmerLayout.startShimmer()
    }
}
