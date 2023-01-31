package com.example.fakestore.presentation.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentProductDetailsBinding
import com.example.fakestore.presentation.util.ext.formatToPrice
import com.example.fakestore.presentation.catalog.ProductListFragment
import com.example.fakestore.presentation.model.CartUiProduct
import com.example.fakestore.presentation.model.UiProduct
import com.example.fakestore.presentation.util.ext.KEY_PRODUCT_ID
import com.example.fakestore.presentation.util.epoxy.decorators.SimpleHorizontalDividerItemDecorator
import com.example.fakestore.presentation.util.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.presentation.util.ext.navigateToProductDetailsFragment
import com.example.fakestore.presentation.util.recyclerview.UiProductAdapter
import com.example.fakestore.presentation.util.ext.setBtnToCartStyle
import com.example.fakestore.presentation.util.ext.setFavoriteIcon
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding by lazy { _binding!! }

    private val viewModel: DetailedViewModel by viewModels()
    private val uiProductAdapter = UiProductAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailsBinding.bind(view)

        arguments?.let {
            val productId: Int = it.getInt(KEY_PRODUCT_ID, -1)
            if (productId != -1) {
                observeUiProduct(productId)
            }
        }
        bottomNavIsVisible(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavIsVisible(true)
        _binding = null
    }

    private fun observeUiProduct(productId: Int) {
        viewModel.store.stateFlow.map { it.productCartInfo }
            .distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) {
                // update quantity
                setQuantity(it.getQuantity(productId))
            }

        val cartQuantity = viewModel.store.stateFlow.value.productCartInfo.getQuantity(productId)

        viewModel.uiProductReducer.reduce(viewModel.store).distinctUntilChanged().asLiveData()
            .observe(viewLifecycleOwner) { uiProducts ->
                val selectedProduct = uiProducts.first { it.product.id == productId }
                val selectedCategory: String = selectedProduct.product.category
                displayUiProduct(CartUiProduct(uiProduct = selectedProduct, cartQuantity))
                val suggestions = uiProducts.filter { it.product.category == selectedCategory }
                    .filter { it.product.id != productId }
                displayProductSuggestions(suggestions)
            }

    }

    private fun setQuantity(quantity: Int) {
        with(binding) {
            btnQuantity.tvQuantity.text = quantity.toString()
        }
    }

    private fun displayProductSuggestions(listSuggestions: List<UiProduct>) {
        binding.rvYouMightAlsLike.run {
            if (!isDirty) {
                addItemDecoration(
                    SimpleHorizontalDividerItemDecorator(ProductListFragment.MARGIN_BOTTOM_RECYCLER_VIEW_ITEM)
                )
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                uiProductAdapter.btnListener = object : GeneralProductClickListener {
                    override fun onFavClickListener(productId: Int) {
                        viewModel.updateFavoriteSet(productId)
                    }

                    override fun onToCartListener(productId: Int) {
                        viewModel.updateCartProductsId(productId)
                    }

                    override fun onCardClickListener(productId: Int) {
                        findNavController().navigateToProductDetailsFragment(
                            productId,
                            R.id.action_productDetailsFragment_self
                        )
                    }
                }
                uiProductAdapter.submitList(listSuggestions)
                adapter = uiProductAdapter
//                val controller = FavoriteItemEpoxyController(viewModel, findNavController())
//                controller.setData(FavFragmentUiState.NonEmpty(listSuggestions))
//                setController(controller)
            }
        }
    }


    private fun displayUiProduct(cartProduct: CartUiProduct) {
        with(binding) {
            setQuantity(cartProduct.quantityInCart)
            cartProduct.uiProduct.product.run {
                btnQuantity.btnPlus.setOnClickListener {
                    viewModel.updateCartQuantity(
                        id,
                        cartProduct.quantityInCart + 1
                    )
                }

                btnQuantity.btnMinus.setOnClickListener {
                    viewModel.updateCartQuantity(
                        id,
                        cartProduct.quantityInCart - 1
                    )
                }

                tvHeadline.text = title
                tvDescription.text = description
                ratingBar.rating = rating.rate
                tvReviews.text = getString(R.string.count_of_reviews, rating.count)

                tvPrice.text = price.formatToPrice()

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

            cartProduct.uiProduct.run {
                btnToCart.setBtnToCartStyle(isInCart, root.context)
                btnToFavorites.setFavoriteIcon(isInFavorites)
            }
        }
    }

    private fun bottomNavIsVisible(isVisible: Boolean) {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.isVisible =
            isVisible
    }

}
