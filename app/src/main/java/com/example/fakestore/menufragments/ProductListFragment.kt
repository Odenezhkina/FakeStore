package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fakestore.R
import com.example.fakestore.animators.FadeInAnimator
import com.example.fakestore.databinding.FragmentProductListBinding
import com.example.fakestore.epoxy.decorators.SimpleGridDividerItemDecorator
import com.example.fakestore.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.model.mapper.ProductMapper
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.network.NetworkService
import com.example.fakestore.recyclerview.UiProductAdapter
import com.example.fakestore.states.ProductListFragmentUiState
import com.example.fakestore.utils.SortManager
import com.example.fakestore.viewmodels.ProductListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.product_list_layout) {
    private var _binding: FragmentProductListBinding? = null
    private val binding: FragmentProductListBinding by lazy { _binding!! }

    private val viewModel: ProductListViewModel by activityViewModels()
    private val uiProductAdapter = UiProductAdapter()

    @Inject
    lateinit var service: NetworkService

    @Inject
    lateinit var productMapper: ProductMapper

    @Inject
    lateinit var sorter: SortManager


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

        with(binding){
            val bottomSheetBehavior = BottomSheetBehavior.from(productListLayout.root).apply {
                isFitToContents = true //
                isHideable = false //prevents the bottom sheet from completely hiding off the screen
                setState(BottomSheetBehavior.STATE_EXPANDED) //initially state to fully expanded
            }
            productListLayout.btnShowFilters.setOnClickListener {
                toggleFilters(bottomSheetBehavior)
            }
        }

        viewModel.init()

//        val epoxyController =
//            UiProductListFragmentController(viewModel, findNavController())

        combine(
            viewModel.uiProductReducer.reduce(viewModel.store),
            viewModel.store.stateFlow.map { it.productFilterInfo }
        ) { uiProducts, productFilterInfo ->

            if (uiProducts.isEmpty()) {
                return@combine ProductListFragmentUiState.Loading
            }

            val uiFilters: Set<UiFilter> =
                productFilterInfo.filterCategory.filters.map { filter ->
                    UiFilter(
                        filter = filter,
                        isSelected = productFilterInfo.filterCategory.selectedFilter?.equals(filter) == true
                    )
                }.toSet()

            return@combine ProductListFragmentUiState.Success(
                products = sorter.sort(uiProducts, productFilterInfo),
                filters = uiFilters
            )
        }.distinctUntilChanged().asLiveData()
            .observe(viewLifecycleOwner) { productListFragmentUiState ->
//                epoxyController.setData(productListFragmentUiState)
                when (productListFragmentUiState) {
                    is ProductListFragmentUiState.Loading -> uiProductAdapter.submitList(null) // todo
                    is ProductListFragmentUiState.Success -> uiProductAdapter.submitList(
                        productListFragmentUiState.products
                    )
                }
            }

        setUpRecycleDemo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun setUpRecycle(epoxyController: UiProductListFragmentController) {
//        with(binding) {
//            productListLayout.rvProducts.run {
//                itemAnimator = FadeInAnimator()
//                if(!isDirty){
//                    addItemDecoration(
//                        SimpleVerticalDividerItemDecorator(
//                            MARGIN_BOTTOM_RECYCLER_VIEW_ITEM
//                        )
//                    )
//                }
//                setController(epoxyController)
//            }
//        }
//    }

    private fun setUpRecycleDemo() {
        with(binding) {
            productListLayout.rvProducts.run {
                layoutManager = GridLayoutManager(requireContext(), 2)
                itemAnimator = FadeInAnimator()
                if (!isDirty) {
                    addItemDecoration(
                        SimpleGridDividerItemDecorator(
                            MARGIN_BOTTOM_RECYCLER_VIEW_ITEM,
                            2
                        )
                    )
                }
                uiProductAdapter.btnListener = object : GeneralProductClickListener {
                    override fun onFavClickListener(productId: Int) {
                        viewModel.updateFavoriteSet(productId)
                    }

                    override fun onToCartListener(productId: Int) {
                        viewModel.updateCartProductsId(productId)
                    }

                    override fun onCardClickListener(productId: Int) {
                        findNavController().navigate(R.id.action_productListFragment_to_productDetailsFragment)
                    }

                }
                adapter = uiProductAdapter
//                adapter = UiProductAdapter()

//                setController(epoxyController)
            }
        }
    }

    private fun toggleFilters(bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>) {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

    companion object {
        const val MARGIN_BOTTOM_RECYCLER_VIEW_ITEM = 16
    }
}
