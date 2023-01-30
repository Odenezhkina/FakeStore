package com.example.fakestore.domain.repository

import com.example.fakestore.domain.model.Product

interface ProductRepository {

    suspend fun fetchAllProducts(): List<Product>

}
