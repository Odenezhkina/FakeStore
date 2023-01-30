package com.example.fakestore.hilt

import com.example.fakestore.data.mapper.ProductMapper
import com.example.fakestore.data.api.NetworkService
import com.example.fakestore.data.repository.ProductRepositoryImpl
import com.example.fakestore.domain.ProductInfoGenerator
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
    ): ProductRepositoryImpl {
        return ProductRepositoryImpl(networkService, ProductMapper(productInfoGenerator))
    }

    @Provides
    @Singleton
    fun providesProductInfoGenerator(): ProductInfoGenerator = ProductInfoGenerator()
}
