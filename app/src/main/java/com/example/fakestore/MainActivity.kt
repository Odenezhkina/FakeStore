package com.example.fakestore

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fakestore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

//    private val viewModel: MainViewModel by lazy {
//        ViewModelProvider(this)[MainViewModel::class.java]
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.favoriteFragment,
                R.id.productListFragment,
                R.id.profileFragment,
                R.id.cartFragment
            )
        )

        // todo fix toolbar color
        val colorDrawable = ColorDrawable(ContextCompat.getColor(this, R.color.beige))
        supportActionBar?.setBackgroundDrawable(colorDrawable)


        setupActionBarWithNavController(navController, appBarConfiguration)
        with(binding) {
            bottomNavigation.setupWithNavController(navController)
        }


        // ui click listener
        // btnToFavorites -> change icon(solid favorite) + change color
        // btnAddToCart -> change icon(done) + change text(added) + change color
        // cardView -> open new fragment
    }

    companion object{
        const val KEY_PRODUCT_ID = "product-id"
    }
}


