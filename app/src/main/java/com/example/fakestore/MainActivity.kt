package com.example.fakestore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestore.databinding.ActivityMainBinding
import com.example.fakestore.epoxy.ProductEpoxyController
import com.example.fakestore.model.ui.UiProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val epoxyController = ProductEpoxyController(resources)
        // setting an empty state for shimmer ??
        epoxyController.setData(emptyList())


        combine(viewModel.store.stateFlow.map { it.products },
            viewModel.store.stateFlow.map { it.favorites }) { listProducts, listFavorites ->
            listProducts.map { product ->
                UiProduct(product = product, isInFavorites = listFavorites.contains(product.id))
            }
        }.distinctUntilChanged().asLiveData().observe(this@MainActivity) { products ->
            epoxyController.setData(products)
        }
        viewModel.refreshProducts()

        with(binding) {
            val itemDecorator =
                DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
            rvProducts.addItemDecoration(itemDecorator)
            rvProducts.setController(epoxyController)
        }


        // ui click listener
        // btnToFavorites -> change icon(solid favorite) + change color
        // btnAddToCart -> change icon(done) + change text(added) + change color
        // cardView -> open new fragment
    }
    }


