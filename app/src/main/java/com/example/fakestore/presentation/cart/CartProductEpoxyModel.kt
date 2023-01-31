package com.example.fakestore.presentation.cart

import androidx.core.view.isVisible
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.CartItemBinding
import com.example.fakestore.presentation.util.ext.formatToPrice
import com.example.fakestore.presentation.model.CartUiProduct
import com.example.fakestore.presentation.util.ViewBindingKotlinModel
import com.example.fakestore.presentation.util.epoxy.listeners.OnCartProductListener
import com.example.fakestore.presentation.util.ext.setFavoriteIcon

// TODO !!!!
class CartProductEpoxyModel(
    val cartProduct: CartUiProduct,
    private val listener: OnCartProductListener? = null
) :
    ViewBindingKotlinModel<CartItemBinding>(R.layout.cart_item) {

//    companion object {
//        private const val FAV_CHANGED_KEY = "FavStatusChanged"
//        private const val QUANTITY_CHANGED_KEY = "QuantityChanged"
//    }

//
//    override fun bind(view: View, payloads: MutableList<Any>) {
//        Log.d("TAGTAG", "hello it is me!!!!")
//        if (payloads.isEmpty()) {
//            super.bind(view, payloads)
//        } else {
//            val myPayload = payloads.first() as Bundle
//            if(myPayload.getBoolean(FAV_CHANGED_KEY)){
//            }
//        }
//    }

//    override fun preBind(view: View, previouslyBoundModel: EpoxyModel<*>?) {
//        val bundle = Bundle()
//        if (previouslyBoundModel is CartProductEpoxyModel) {
//            previouslyBoundModel.cartProduct.run {
//                if(quantityInCart != cartProduct.quantityInCart){
//                    bundle.putBoolean(QUANTITY_CHANGED_KEY, true)
//                }
//                if (uiProduct.isInFavorites != cartProduct.uiProduct.isInFavorites){
//                    bundle.putBoolean(FAV_CHANGED_KEY, true)
//                }
//            }
//        }
//        bind(view, mutableListOf(bundle))
////        super.preBind(view, previouslyBoundModel)
//    }

    override fun CartItemBinding.bind() {
        val id = cartProduct.uiProduct.product.id
        cartProduct.uiProduct.product.run {
            tvHeadline.text = title
            tvPrice.text = price.formatToPrice()

            ivImage.load(image) {
                listener { _, _ ->
                    pbLoadingImage.isVisible = false
                }
            }
            ratingBar.rating = rating.rate

            btnToFavorites.setOnClickListener {
                listener?.onFavClickListener(id)
            }
//            btnDelete.setOnClickListener {
//                listener?.delOnClickListener(id)
//            }
        }
        with(cartProduct) {
            btnQuantity.tvQuantity.text = quantityInCart.toString()
            btnToFavorites.setFavoriteIcon(uiProduct.isInFavorites)
            btnQuantity.btnMinus.setOnClickListener {
                listener?.quantityChangeListener(id, quantityInCart - 1)
            }
            btnQuantity.btnPlus.setOnClickListener {
                listener?.quantityChangeListener(id, quantityInCart + 1)
            }
            root.setOnClickListener {
                listener?.onCardClickListener(id)
            }
        }

    }
}
