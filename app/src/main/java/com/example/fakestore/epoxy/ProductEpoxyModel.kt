package com.example.fakestore.epoxy

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.model.domain.Product
import java.text.NumberFormat

data class ProductEpoxyModel(
    val product: Product?,
//    val onFavoriteIconClicked: (Int) -> Unit,
//    val onUiProductClicked: (Int) -> Unit,
//    val onAddToCartClicked: (Int) -> Unit
) : ViewBindingKotlinModel<ProductItemBinding>(R.layout.product_item) {

    private val currencyFormatter = NumberFormat.getCurrencyInstance()

    override fun ProductItemBinding.bind() {
        product?.let{

            tvHeadline.text = product.title
            tvDescription.text = product.description
            tvPrice.text = currencyFormatter.format(product.price)

            pbLoadingImage.isVisible = true
            ivImage.load(data = product.image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }

        }
    }

}
