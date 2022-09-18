package com.example.fakestore.epoxy

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.model.ui.UiProduct
import java.text.NumberFormat
import java.util.*

data class ProductEpoxyModel(
    val product: UiProduct?,
//    val onFavoriteIconClicked: (Int) -> Unit,
//    val onUiProductClicked: (Int) -> Unit,
//    val onAddToCartClicked: (Int) -> Unit
) : ViewBindingKotlinModel<ProductItemBinding>(R.layout.product_item) {

    private val currencyFormatter = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance("USD")
    }

    override fun ProductItemBinding.bind() {
        // product not null

        shimmerLayout.isVisible = product == null
        cardview.isInvisible = product == null

        product?.run {
            shimmerLayout.stopShimmer()

            tvHeadline.text = product.title
            tvDescription.text = product.description
            tvPrice.text = currencyFormatter.format(product.price)

            btnToFavorites.setIconResource(if (isInFavorites) R.drawable.ic_round_favorite_24 else R.drawable.ic_round_favorite_border_24)

            pbLoadingImage.isVisible = true
            ivImage.load(data = product.image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }
        } ?: shimmerLayout.startShimmer()
    }

}
