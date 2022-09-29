package com.example.fakestore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.example.fakestore.MainActivity
import com.example.fakestore.MainViewModel
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentProductDetailsBinding
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.uimanager.ProductListUiManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding by lazy { _binding!! }

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val productId: Int = it.getInt(ProductListUiManager.KEY_PRODUCT_ID, -1)
            if (productId != -1) {
                observeUiProduct(productId)
            }
        }

        bottomNavIsVisible(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavIsVisible(true)
        _binding = null
    }

    private fun observeUiProduct(productId: Int) {
        // todo fix repeating (combining here and in product list)
        combine(viewModel.store.stateFlow.map { it.products },
            viewModel.store.stateFlow.map { it.favoriteProductsIds }) { listProducts, listFavorites ->
            listProducts.map { product ->
                UiProduct(
                    product = product,
                    isInFavorites = listFavorites.contains(product.id)
                )
            }
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) { products ->
            products.forEach { uiproduct ->
                if (uiproduct.product.id == productId) {
                    displayUiProduct(uiproduct)
                }
            }
        }
        viewModel.refreshProducts()
    }


    private fun displayUiProduct(uiproduct: UiProduct) {
        with(binding) {
            uiproduct.product.run {
                tvHeadline.text =  title
                tvCategory.text =  category
                tvDescription.text =  "$description $description $description $description $description $description $description $description $description $description $description $description $description $description $description $description $description $description $description $description"
                ratingBar.rating = rating.rate
                tvReviews.text =
                    getString(R.string.count_of_reviews, rating.count)

                pbLoadingImage.isVisible = true
                ivExpanded.load(data = image) {
                    listener { _, _ ->
                        pbLoadingImage.isVisible = false
                    }
                }
            }

            btnToFavorites.setIconResource(ProductListUiManager.getResFavoriteIconId(uiproduct.isInFavorites))
            btnToFavorites.setOnClickListener {
                viewModel.updateFavoriteSet(uiproduct.product.id)
            }
        }
    }


    private fun bottomNavIsVisible(isVisible: Boolean){
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.isVisible = isVisible
    }
}
