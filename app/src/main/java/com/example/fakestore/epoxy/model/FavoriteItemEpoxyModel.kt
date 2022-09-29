package com.example.fakestore.epoxy.model

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.FavoriteItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.uimanager.ProductListUiManager
import java.text.NumberFormat
import java.util.*

class FavoriteItemEpoxyModel(
    private val favProduct: UiProduct,
    private val onFavoriteIconListener: (Int) -> Unit
) :
    ViewBindingKotlinModel<FavoriteItemBinding>(R.layout.favorite_item) {
    // todo fix repeating currencyFormatter in models

    private val currencyFormatter = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance("USD")
    }

    override fun FavoriteItemBinding.bind() {
        favProduct.product.run {
            tvHeadline.text = title
            ivImage.load(data = image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }
            tvPrice.text = currencyFormatter.format(price)
            ratingBar.rating = rating.rate
            tvCategory.text = category
        }
        btnToFavorites.setIconResource(ProductListUiManager.getResFavoriteIconId(favProduct.isInFavorites))
        btnToFavorites.setOnClickListener {
            onFavoriteIconListener(favProduct.product.id)
        }
        cardview.setOnClickListener{
            //onCardClickListener(product.id)
        }
    }

}
