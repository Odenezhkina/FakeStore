package com.example.fakestore.data.repository

import com.example.fakestore.data.api.NetworkService
import com.example.fakestore.domain.repository.ProductRepository
import com.example.fakestore.domain.model.Product
import com.example.fakestore.data.mapper.ProductMapper
import javax.inject.Inject

class ProductRepositoryImpl
@Inject constructor(
    private val service: NetworkService,
    private val mapper: ProductMapper
) : ProductRepository {

    override suspend fun fetchAllProducts(): List<Product> {
        return service.getAllProducts().body()?.map {
            mapper.buildFrom(networkProduct = it)
        } ?: emptyList()
    }
}
