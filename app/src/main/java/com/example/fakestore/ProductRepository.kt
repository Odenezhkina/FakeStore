package com.example.fakestore

import com.example.fakestore.model.domain.Product
import com.example.fakestore.model.mapper.ProductMapper
import com.example.fakestore.network.NetworkService
import javax.inject.Inject

class ProductRepository
@Inject constructor(
    private val service: NetworkService,
    private val mapper: ProductMapper
) {

    suspend fun fetchAllProducts(): List<Product> {
        return service.getAllProducts().body()?.map {
            mapper.buildFrom(networkProduct = it)
        } ?: emptyList()
    }

}
