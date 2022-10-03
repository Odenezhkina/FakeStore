package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.fakestore.viewmodels.MainViewModel
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentProductListBinding
import com.example.fakestore.epoxy.controllers.UiProductListFragmentController
import com.example.fakestore.model.mapper.ProductMapper
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.model.ui.UiFilter
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

        val viewModel: MainViewModel by viewModels()
        val epoxyController =
            UiProductListFragmentController(resources, viewModel, findNavController())

        // todo fix shimmer
        // todo fix filtering


        combine(
            viewModel.uiProductReducer.reduce(viewModel.store),
            viewModel.store.stateFlow.map { it.productFilterInfo }
        ) { uiProducts, productFilterInfo ->

            val uiFilters: Set<UiFilter> =
                productFilterInfo.filters.map { filter ->
                    UiFilter(
                        filter = filter,
                        isSelected = productFilterInfo.selectedFilter?.equals(filter) == true
                    )
                }.toSet()

            val filteredProducts = if (productFilterInfo.selectedFilter == null) {
                uiProducts
            } else {
                uiProducts.filter { uiProduct ->
                    uiProduct.product.category == productFilterInfo.selectedFilter.title
                }
            }

            ProductListFragmentUiState(products = filteredProducts, filters = uiFilters)
        }.distinctUntilChanged().asLiveData()
            .observe(viewLifecycleOwner) { productListFragmentUiState ->
                epoxyController.setData(productListFragmentUiState)
            }

        viewModel.refreshProducts()
        with(binding) {
            rvProducts.setController(epoxyController)
        }
    }


//    viewModel.store.stateFlow.run
//    {
//        combine(map { it.products },
//            map { it.favoriteProductsIds },
//            map { it.productCartInfo },
//            map { it.productFilterInfo }) { listProducts, listFavorites, productCartInfo, productFilterInfo ->
//
//            var uiProducts: List<UiProduct> = listProducts.map { product ->
//                UiProduct(
//                    product = product,
//                    isInFavorites = listFavorites.contains(product.id),
//                    isInCart = productCartInfo.isInCart(product.id)
//                )
//            }
//
//            val selectedFilter: Filter? = productFilterInfo.selectedFilter
//            val uiFilters: Set<UiFilter> =
//                productFilterInfo.filters.map {
//                    UiFilter(
//                        filter = it,
//                        isSelected = selectedFilter?.title == it.title
//                    )
//                }
//                    .toSet()
//            selectedFilter?.let { selectedFilter ->
//                uiProducts =
//                    uiProducts.filter { uiProduct ->
//                        uiProduct.product.category == selectedFilter.title
//                    }
//                        .toList()
//            }
//
//            ProductListFragmentUiState(products = uiProducts, filters = uiFilters)
//        }.distinctUntilChanged().asLiveData()
//            .observe(viewLifecycleOwner) { productListFragmentUiState ->
//                epoxyController.setData(productListFragmentUiState)
//            }
//
//        viewModel.refreshProducts()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
