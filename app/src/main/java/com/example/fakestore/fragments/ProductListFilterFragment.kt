package com.example.fakestore.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductListFiltersLayoutBinding
import com.example.fakestore.epoxy.controllers.UiFilterItemController
import com.example.fakestore.managers.uimanager.MainUiManager
import com.example.fakestore.managers.uimanager.MainUiManager.formatToPrice
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.redux.ApplicationState.ProductFilterInfo.SortType.Companion.SORT_TYPE_CHEAPEST_FIRST
import com.example.fakestore.redux.ApplicationState.ProductFilterInfo.SortType.Companion.SORT_TYPE_MOST_EXPENSIVE_FIRST
import com.example.fakestore.redux.ApplicationState.ProductFilterInfo.SortType.Companion.SORT_TYPE_MOST_POPULAR
import com.example.fakestore.viewmodels.ProductListViewModel
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ProductListFilterFragment : Fragment(R.layout.product_list_filters_layout) {
    private var _binding: ProductListFiltersLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = ProductListFiltersLayoutBinding.bind(view)

        with(binding) {
            val viewModel: ProductListViewModel by activityViewModels()
            val epoxyController =
                UiFilterItemController(viewModel)
            filtersEpoxyCarousel.setController(epoxyController)

            viewModel.store.stateFlow.map { it.productFilterInfo }
                .distinctUntilChanged()
                .asLiveData()
                .observe(viewLifecycleOwner) { productFilterInfo ->

                    // set up sort type
                    productFilterInfo.sortType.run {
                        when (sortType) {
                                SORT_TYPE_MOST_POPULAR -> {
                                    btnMostPopular.isActivated = true
                                }
                                SORT_TYPE_CHEAPEST_FIRST -> {
                                    btnCheapest.isActivated = true
                                }
                                SORT_TYPE_MOST_EXPENSIVE_FIRST -> {
                                    btnMostExpensive.isActivated = true
                                }
                                else -> {
                                    // some error
                                }
                            }


                        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                            when (checkedId) {
                                R.id.btn_most_popular -> {
                                    viewModel.updateSortType(SORT_TYPE_MOST_POPULAR)
                                }
                                R.id.btn_cheapest -> {
                                    viewModel.updateSortType(SORT_TYPE_CHEAPEST_FIRST)
                                }
                                R.id.btn_most_expensive -> {
                                    viewModel.updateSortType(SORT_TYPE_MOST_EXPENSIVE_FIRST)
                                }
                            }
                        }

                    }


                    // set up range sort
                    productFilterInfo.rangeSort.run {
                        toCost?.let {
                            rangeSliderCost.valueFrom = fromCost.toFloat()
                            rangeSliderCost.valueTo = it.toFloat()
                            rangeSliderCost.values = listOf(fromCost.toFloat(), it.toFloat())
                        }

                        rangeSliderCost.setLabelFormatter { value: Float ->
                            value.toBigDecimal().formatToPrice()
                        }

                        rangeSliderCost.addOnSliderTouchListener(object :
                            RangeSlider.OnSliderTouchListener {
                            // todo do better
                            override fun onStartTrackingTouch(slider: RangeSlider) {
                                etFrom.setText(slider.values[0].toString())
                                etTo.setText(slider.values[1].toString())
                            }

                            override fun onStopTrackingTouch(slider: RangeSlider) {
                                etFrom.setText(slider.values[0].toString())
                                etTo.setText(slider.values[1].toString())

                                viewModel.updateRangeSort(
                                    slider.values[0].toBigDecimal(),
                                    slider.values[1].toBigDecimal()
                                )
                            }
                        }
                        )
                    }

                    // set up category filtering
                    productFilterInfo.filterCategory.run {
                        val uifilters = filters.map { filter ->
                            UiFilter(
                                filter = filter,
                                isSelected = selectedFilter?.equals(
                                    filter
                                ) == true
                            )
                        }.toSet()
                        epoxyController.setData(uifilters)
                    }


                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
