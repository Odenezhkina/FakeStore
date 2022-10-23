package com.example.fakestore.epoxy.model

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.FavoriteItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.epoxy.controllers.GeneralProductClickListener
import com.example.fakestore.managers.uimanager.MainUiManager.formatToPrice
import com.example.fakestore.managers.uimanager.MainUiManager.setFavoriteIcon
import com.example.fakestore.managers.uimanager.MainUiManager.setInCartStyle
import com.example.fakestore.model.ui.UiProduct

class FavoriteItemEpoxyModel(
    private val favProduct: UiProduct,
    private val listener: GeneralProductClickListener = null
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
                listener?.onFavoriteIconListener(id)
            }
            btnToCart.setOnClickListener {
                listener?.onAddToCartClickListener(id)
            }

            cardview.setOnClickListener {
                listener?.onFavItemClickListener(id)
            }
        }

    }
}
