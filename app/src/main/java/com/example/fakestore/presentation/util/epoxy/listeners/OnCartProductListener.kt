package com.example.fakestore.presentation.util.epoxy.listeners

interface OnCartProductListener : GeneralProductClickListener {
    fun delOnClickListener(productId: Int)
    fun quantityChangeListener(productId: Int, updatedQuantity: Int)
}
