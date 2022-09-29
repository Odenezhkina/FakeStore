package com.example.fakestore.epoxy.model

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.uimanager.ProductListUiManager
import java.text.NumberFormat
import java.util.*

data class UiProductEpoxyModel(
    val product: UiProduct?,
    val onFavoriteIconClicked: (Int) -> Unit,
    val onCardClickListener: (Int) -> Unit
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
            tvCategory.text = product.category
            tvPrice.text = currencyFormatter.format(product.price)
            btnToFavorites.setIconResource(ProductListUiManager.getResFavoriteIconId(isInFavorites))

            pbLoadingImage.isVisible = true
            ivImage.load(data = product.image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }

            // listeners
            btnToFavorites.setOnClickListener {
                onFavoriteIconClicked(product.id)
            }
            cardview.setOnClickListener{
                onCardClickListener(product.id)
            }

        } ?: shimmerLayout.startShimmer()
    }
}
