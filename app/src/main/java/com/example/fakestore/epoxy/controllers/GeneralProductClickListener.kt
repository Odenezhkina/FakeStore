package com.example.fakestore.epoxy.controllers

interface GeneralProductClickListener {
    fun onFavClickListener(productId: Int)
    fun onToCartListener(productId: Int)
    fun onCardClickListener(productId: Int)
}
