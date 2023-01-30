package com.example.fakestore.presentation.util.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.UiproductItemDemoBinding
import com.example.fakestore.presentation.util.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.presentation.model.UiProduct
import com.example.fakestore.utils.uimanager.MainUiManager.formatToPrice
import com.example.fakestore.utils.uimanager.MainUiManager.setFavoriteIcon
import com.example.fakestore.utils.uimanager.MainUiManager.setInCartStyle

class UiProductViewHolder(
    private val binding: UiproductItemDemoBinding,
    private val listener: GeneralProductClickListener? = null
) :
    ViewHolder(binding.root) {

    fun bind(uiProduct: UiProduct) {
        with(binding) {
            uiProduct.product.run {
                tvHeadline.text = title
                tvCategory.text = category
                tvPrice.text = price.formatToPrice()

                btnToFavorites.setFavoriteIcon(uiProduct.isInFavorites)

                ratingBar.rating = rating.rate
                tvCountOfReviews.text =
                    itemView.context.getString(
                        R.string.count_of_reviews,
                        rating.count
                    )

                pbLoadingImage.isVisible = true
                ivImage.load(data = image) {
                    listener { _, _ ->
                        pbLoadingImage.isVisible = false
                    }
                }

                btnToCart.setInCartStyle(uiProduct.isInCart, root.context)

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

    companion object {
        fun create(parent: ViewGroup, listener: GeneralProductClickListener? = null) =
            UiProductViewHolder(
                UiproductItemDemoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                listener
            )
    }
}
