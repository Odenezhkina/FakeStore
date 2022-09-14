package com.example.fakestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fakestore.databinding.ActivityMainBinding
import com.example.fakestore.epoxy.ProductEpoxyController
import com.example.fakestore.model.domain.Product
import com.example.fakestore.model.mapper.ProductMapper
import com.example.fakestore.network.NetworkService
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    @Inject
    lateinit var service: NetworkService
    @Inject
    lateinit var productMapper: ProductMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val epoxyController = ProductEpoxyController(resources)

        lifecycleScope.launchWhenStarted {
            val response = service.getAllProducts()
            val domainProductList = response.body()?.let { listNetworkProducts ->
                listNetworkProducts.map {
                    productMapper.buildFrom(networkProduct = it)
                }
            }
            epoxyController.setData(domainProductList)
        }

        with(binding){
            val itemDecorator = DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
            rvProducts.addItemDecoration(itemDecorator)
            rvProducts.setController(epoxyController)
        }




        // ui click listener
        // btnToFavorites -> change icon(solid favorite) + change color
        // btnAddToCart -> change icon(done) + change text(added) + change color
        // cardView -> open new fragment
    }
}
