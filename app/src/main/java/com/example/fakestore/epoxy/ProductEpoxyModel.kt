package com.example.fakestore.epoxy

import android.util.Log
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.model.domain.Product
import java.text.NumberFormat
import java.util.*

data class ProductEpoxyModel(
    val product: Product?,
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

        product?.let {
            shimmerLayout.stopShimmer()

            tvHeadline.text = product.title
            tvDescription.text = product.description
            tvPrice.text = currencyFormatter.format(product.price)

            pbLoadingImage.isVisible = true
            ivImage.load(data = product.image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }
        } ?: shimmerLayout.startShimmer()
    }

}
