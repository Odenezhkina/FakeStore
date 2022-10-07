package com.example.fakestore.epoxy.model

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.FavoriteItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.uimanager.MainUiManager

class FavoriteItemEpoxyModel(
    private val res: Resources,
    private val favProduct: UiProduct,
    private val onFavoriteIconListener: (Int) -> Unit,
    private val onAddToCartClickListener: (Int) -> Unit,
    private val onFavItemClickListener: (Int) -> Unit
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
            tvPrice.text = MainUiManager.formatPrice(price)
            ratingBar.rating = rating.rate
            tvCategory.text = category
            tvCountOfReviews.text = res.getString(R.string.count_of_reviews, rating.count)
        }

        val backgroundColorIconIds: Pair<Int, Int> =
            MainUiManager.getCartUi(favProduct.isInCart)
        btnToCart.setIconResource(backgroundColorIconIds.second)
        btnToCart.setBackgroundColor(
            ResourcesCompat.getColor(
                res,
                backgroundColorIconIds.first,
                null
            )
        )

        btnToFavorites.setIconResource(MainUiManager.getResFavoriteIconId(favProduct.isInFavorites))

        btnToFavorites.setOnClickListener {
            onFavoriteIconListener(favProduct.product.id)
        }
        btnToCart.setOnClickListener {
            onAddToCartClickListener(favProduct.product.id)
        }

        cardview.setOnClickListener {
            onFavItemClickListener(favProduct.product.id)
        }

    }
}
