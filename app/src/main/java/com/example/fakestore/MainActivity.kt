package com.example.fakestore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.airbnb.epoxy.Carousel
import com.example.fakestore.databinding.ActivityMainBinding
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var store: Store<ApplicationState>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController


        with(binding) {
            bottomNavigation.setupWithNavController(navController)
        }

        // To prevent snapping in carousels
        Carousel.setDefaultGlobalSnapHelperFactory(null)

        store.stateFlow.map {
            it.productCartInfo
        }.distinctUntilChanged().asLiveData().observe(this) { productCartInfo ->
            binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                backgroundColor = getColor(R.color.orange)
                number = productCartInfo.countProductsInCart()
                isVisible = productCartInfo.countProductsInCart() > 0
            }
        }

    }
}


