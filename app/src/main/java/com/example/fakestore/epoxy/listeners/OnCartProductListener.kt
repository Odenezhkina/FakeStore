package com.example.fakestore.epoxy.listeners

interface OnCartProductListener : GeneralProductClickListener {
    fun delOnClickListener(productId: Int)
    fun quantityChangeListener(productId: Int, updatedQuantity: Int)
}
