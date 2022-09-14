package com.example.fakestore.network

import com.example.fakestore.model.network.NetworkProduct
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<NetworkProduct>>
}
