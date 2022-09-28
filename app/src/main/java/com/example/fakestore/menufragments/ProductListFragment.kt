package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.fakestore.MainViewModel
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentProductListBinding
import com.example.fakestore.epoxy.UiProductListFragmentController
import com.example.fakestore.model.mapper.ProductMapper
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.model.ui.UiProduct
import com.example.fakestore.network.NetworkService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {
    private var _binding: FragmentProductListBinding? = null
    private val binding: FragmentProductListBinding by lazy { _binding!! }

    @Inject
    lateinit var service: NetworkService

    @Inject
    lateinit var productMapper: ProductMapper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainViewModel: MainViewModel by viewModels()
        val epoxyController =
            UiProductListFragmentController(resources, mainViewModel, findNavController())

        // todo fix shimmer

        combine(mainViewModel.store.stateFlow.map { it.products },
            mainViewModel.store.stateFlow.map { it.favoriteProductsIds },
            mainViewModel.store.stateFlow.map { it.productFilterInfo })
        { listProducts, listFavorites, productFilterInfo ->

            var products = listProducts.map { product ->
                UiProduct(product = product, isInFavorites = listFavorites.contains(product.id))
            }

            val filters: Set<UiFilter> = productFilterInfo.filters.map { filter ->
                // on default isSelected is false
                return@map when {
                    productFilterInfo.selectedFilter == null -> {
                        UiFilter(filter = filter) // on default isSelected is false
                    }
                    productFilterInfo.selectedFilter.title != filter.title -> {
                        UiFilter(filter = filter)
                    }
                    else -> {
                        UiFilter(filter = filter, isSelected = true)
                    }
                }
            }.toSet()

            // todo fix filtration
            val selectedFilter: UiFilter? = filters.find { it.isSelected }
            selectedFilter?.let {
                products = products.filter {
                    it.product.category == selectedFilter.filter.title
                }.toList()
            }
            // if there is selectedFilter -> filter data
            // else -> do not filter

            ProductListFragmentUiState(products = products, filters = filters)
        }.distinctUntilChanged().asLiveData()
            .observe(viewLifecycleOwner) { productListFragmentUiState ->
                epoxyController.setData(productListFragmentUiState)
            }
        mainViewModel.refreshProducts()

        with(binding) {
            rvProducts.setController(epoxyController)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
