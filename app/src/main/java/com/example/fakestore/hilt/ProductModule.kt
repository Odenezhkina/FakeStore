package com.example.fakestore.hilt

import com.example.fakestore.model.mapper.ProductMapper
import com.example.fakestore.network.NetworkService
import com.example.fakestore.repository.ProductRepository
import com.example.fakestore.utils.ProductInfoGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun providesProductRepository(
        networkService: NetworkService,
        productInfoGenerator: ProductInfoGenerator
    ): ProductRepository {
        return ProductRepository(networkService, ProductMapper(productInfoGenerator))
    }

    @Provides
    @Singleton
    fun providesProductInfoGenerator(): ProductInfoGenerator = ProductInfoGenerator()
}
