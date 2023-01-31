package com.example.fakestore.presentation.favorite

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.FavoriteItemBinding
import com.example.fakestore.presentation.util.ext.formatToPrice
import com.example.fakestore.presentation.model.UiProduct
import com.example.fakestore.presentation.util.ViewBindingKotlinModel
import com.example.fakestore.presentation.util.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.presentation.util.ext.setFavoriteIcon
import com.example.fakestore.presentation.util.ext.setInCartStyle

class FavoriteItemEpoxyModel(
    private val favProduct: UiProduct,
    private val listener: GeneralProductClickListener? = null
) :
    ViewBindingKotlinModel<FavoriteItemBinding>(R.layout.favorite_item) {

    override fun FavoriteItemBinding.bind() {
        favProduct.product.run {
            tvHeadline.text = title
            ivImage.load(data = image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }
            tvPrice.text = price.formatToPrice()
            ratingBar.rating = rating.rate
            tvCategory.text = category
            tvCountOfReviews.text =
                this@bind.root.context.getString(R.string.count_of_reviews, rating.count)
        }

        btnToCart.setInCartStyle(favProduct.isInCart, this.root.context)
        btnToFavorites.setFavoriteIcon(favProduct.isInFavorites)

        favProduct.product.run {
            btnToFavorites.setOnClickListener {
                listener?.onFavClickListener(id)
            }
            btnToCart.setOnClickListener {
                listener?.onToCartListener(id)
            }

            cardview.setOnClickListener {
                listener?.onCardClickListener(id)
            }
        }

    }
}
