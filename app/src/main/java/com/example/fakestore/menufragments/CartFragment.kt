package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestore.viewmodels.MainViewModel
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentCartBinding
import com.example.fakestore.epoxy.controllers.FavoriteItemEpoxyController
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.model.ui.detailed.UiProductDetailed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding by lazy { _binding!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo reaping combine here, in ProductListFragment and DetailProductFragment
        initObservers()
    }

    private fun initObservers() {
        val viewModel: MainViewModel by viewModels()
//        val epoxyController = FavoriteItemEpoxyController(viewModel)

        viewModel.store.stateFlow.run {
            combine(
                map { it.products },
                map { it.favoriteProductsIds },
                map { it.productCartInfo }
            ) { listProducts, listFavorites, productCartInfo ->

                // todo combine cart stuff
                val cartProducts: List<UiProductDetailed> = listProducts.map { product ->
                    UiProduct(
                        product = product,
                        isInFavorites = listFavorites.contains(product.id),
                        isInCart = productCartInfo.isInCart(product.id)
                    )
                }.filter { it.isInCart }
                    .map { UiProductDetailed(it, productCartInfo.getQuantity(it.product.id)) }

                cartProducts
            }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) { products ->
//                epoxyController.setData(products)
//                manageUi(products.isEmpty(), epoxyController)
            }
            viewModel.refreshProducts()
        }
    }

    private fun manageUi(isProductsEmpty: Boolean, epoxyController: FavoriteItemEpoxyController) {
        with(binding) {
            tvGoToCatalog.isVisible = isProductsEmpty
            tvNoProductTitle.isVisible = isProductsEmpty

            rvCart.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvCart.setController(epoxyController)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
