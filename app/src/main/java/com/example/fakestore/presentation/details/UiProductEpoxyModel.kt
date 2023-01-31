package com.example.fakestore.presentation.details

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.presentation.util.ext.formatToPrice
import com.example.fakestore.presentation.model.UiProduct
import com.example.fakestore.presentation.util.ViewBindingKotlinModel
import com.example.fakestore.presentation.util.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.presentation.util.ext.setFavoriteIcon
import com.example.fakestore.presentation.util.ext.setInCartStyle

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
