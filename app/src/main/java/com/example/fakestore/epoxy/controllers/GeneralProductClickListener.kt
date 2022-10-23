package com.example.fakestore.epoxy.controllers

interface GeneralProductClickListener {
    fun onFavClickListener(productId: Int)
    fun onToCardListener(productId: Int)
    fun onCardClickListener(productId: Int)
}
