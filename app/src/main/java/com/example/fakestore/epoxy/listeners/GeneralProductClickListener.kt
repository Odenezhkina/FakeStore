package com.example.fakestore.epoxy.listeners

interface GeneralProductClickListener {
    fun onFavClickListener(productId: Int)
    fun onToCartListener(productId: Int)
    fun onCardClickListener(productId: Int)
}
