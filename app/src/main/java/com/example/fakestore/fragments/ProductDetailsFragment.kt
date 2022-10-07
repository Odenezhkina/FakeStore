package com.example.fakestore.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentProductDetailsBinding
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.uimanager.MainUiManager
import com.example.fakestore.viewmodels.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding by lazy { _binding!! }

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val productId: Int = it.getInt(MainUiManager.KEY_PRODUCT_ID, -1)
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
        viewModel.uiProductReducer.reduce(viewModel.store)
            .distinctUntilChanged()
            .asLiveData()
            .observe(viewLifecycleOwner) { products ->
                products.forEach { uiproduct ->
                    if (uiproduct.product.id == productId) {
                        displayUiProduct(uiproduct)
                    }
                }
            }
    }


    private fun displayUiProduct(uiProduct: UiProduct) {
        with(binding) {
            uiProduct.product.run {
                tvHeadline.text = title
                tvCategory.text = category
                tvDescription.text = "$description $description $description $description"
                ratingBar.rating = rating.rate
                tvReviews.text = getString(R.string.count_of_reviews, rating.count)

                tvPrice.text = MainUiManager.formatPrice(price)

                pbLoadingImage.isVisible = true
                ivExpanded.load(data = image) {
                    listener { _, _ ->
                        pbLoadingImage.isVisible = false
                    }
                }

                btnToFavorites.setOnClickListener {
                    viewModel.updateFavoriteSet(id)
                }
                btnToCart.setOnClickListener {
                    viewModel.updateCartProductsId(id)
                }
            }

            val backgroundColorIconIds: Pair<Int, Int> = MainUiManager.getCartUi(uiProduct.isInCart)
            btnToCart.setImageResource(backgroundColorIconIds.second)
            btnToCart.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), backgroundColorIconIds.first)
            )

            btnToFavorites.setIconResource(
                MainUiManager.getResFavoriteIconId(
                    uiProduct.isInFavorites
                )
            )

        }
    }

    private fun bottomNavIsVisible(isVisible: Boolean) {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.isVisible =
            isVisible
    }

}
