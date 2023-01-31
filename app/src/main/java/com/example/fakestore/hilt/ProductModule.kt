package com.example.fakestore.hilt

import com.example.fakestore.data.api.NetworkService
import com.example.fakestore.data.repository.ProductRepositoryImpl
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
        networkService: NetworkService
    ): ProductRepositoryImpl {
        return ProductRepositoryImpl(networkService)
    }

}
