package com.example.fakestore.data.api

import com.example.fakestore.data.model.NetworkProduct
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<NetworkProduct>>
}
