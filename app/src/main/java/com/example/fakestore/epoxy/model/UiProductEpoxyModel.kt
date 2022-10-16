package com.example.fakestore.epoxy.model

import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.managers.uimanager.MainUiManager
import com.example.fakestore.model.ui.UiProduct

data class UiProductEpoxyModel(
    private val product: UiProduct?,
    private val onFavoriteIconClicked: (Int) -> Unit,
    private val onCardClickListener: (Int) -> Unit,
    private val onCartClickListener: (Int) -> Unit
) : ViewBindingKotlinModel<ProductItemBinding>(R.layout.product_item) {

    override fun ProductItemBinding.bind() {
        // product not null
        shimmerLayout.isVisible = product == null
        cardview.isInvisible = product == null

        product?.run {
            shimmerLayout.stopShimmer()

            tvHeadline.text = product.title
            tvCategory.text = product.category
            tvPrice.text = MainUiManager.formatPrice(product.price)
            btnToFavorites.setIconResource(MainUiManager.getResFavoriteIconId(isInFavorites))
            ratingBar.rating = product.rating.rate

            pbLoadingImage.isVisible = true
            ivImage.load(data = product.image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }

            val backgroundColorIconIds: Pair<Int, Int> = MainUiManager.getCartUi(isInCart)
            btnToCart.setIconResource(backgroundColorIconIds.second)
            btnToCart.setBackgroundColor(
                ResourcesCompat.getColor(
                    this@bind.root.context.resources,
                    backgroundColorIconIds.first,
                    null
                )
            )

            // listeners
            btnToFavorites.setOnClickListener {
                onFavoriteIconClicked(product.id)
            }

            btnToCart.setOnClickListener {
                onCartClickListener(product.id)
            }

            cardview.setOnClickListener {
                onCardClickListener(product.id)
            }

        } ?: shimmerLayout.startShimmer()
    }
}
