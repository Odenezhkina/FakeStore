package com.example.fakestore.epoxy.controllers

import androidx.navigation.NavController
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.R
import com.example.fakestore.epoxy.listeners.OnCartProductListener
import com.example.fakestore.epoxy.model.CartProductEpoxyModel
import com.example.fakestore.epoxy.model.EmptyListEpoxyModel
import com.example.fakestore.utils.uimanager.navigateToProductDetailsFragment
import com.example.fakestore.states.CartFragmentUiState
import com.example.fakestore.viewmodels.CartViewModel
import java.util.*

class CartProductEpoxyController(
    private val viewModel: CartViewModel,
    private val navController: NavController
) :
    TypedEpoxyController<CartFragmentUiState>() {

    override fun buildModels(data: CartFragmentUiState?) {
        when (data) {
            null, is CartFragmentUiState.Empty -> EmptyListEpoxyModel(::goToCatalog).id(
                UUID.randomUUID().toString()
            ).addTo(this)
            is CartFragmentUiState.NonEmpty ->
                data.products.forEach {
                    CartProductEpoxyModel(
                        cartProduct = it,
                        listener = object : OnCartProductListener {
                            override fun onFavClickListener(productId: Int) {
                                viewModel.updateFavoriteSet(productId)
                            }

                            override fun onToCartListener(productId: Int) {
                                // todo
                            }

                            override fun onCardClickListener(productId: Int) {
                                navController.navigateToProductDetailsFragment(
                                    productId,
                                    R.id.action_cartFragment_to_productDetailsFragment
                                )
                            }

                            override fun delOnClickListener(productId: Int) {
                                viewModel.updateCartProductsId(productId)
                            }

                            override fun quantityChangeListener(
                                productId: Int,
                                updatedQuantity: Int
                            ) {
                                viewModel.updateCartQuantity(productId, updatedQuantity)
                            }
                        }
                    ).id(it.toString()).addTo(this)
                }

        }
    }

    private fun goToCatalog() {
        navController.navigate(R.id.action_cartFragment_to_productListFragment)
    }

}

