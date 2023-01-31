package com.example.fakestore.presentation.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductListFiltersLayoutBinding
import com.example.fakestore.presentation.ApplicationState.ProductFilterInfo
import com.example.fakestore.presentation.catalog.ProductListViewModel
import com.example.fakestore.presentation.model.UiFilter
import com.example.fakestore.presentation.util.ext.formatToPrice
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class ProductListFilterFragment : Fragment(R.layout.product_list_filters_layout) {
    private var _binding: ProductListFiltersLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()
    private var epoxyController: UiFilterItemController? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProductListFiltersLayoutBinding.bind(view)

        with(binding) {
            epoxyController =
                UiFilterItemController(viewModel)
            epoxyController?.let { filtersEpoxyCarousel.setController(it) }

            initObservers()
        }
    }

    private fun initObservers() {
        viewModel.store.stateFlow
            .map { it.productFilterInfo }
            .distinctUntilChanged()
            .asLiveData()
            .observe(viewLifecycleOwner) { productFilterInfo ->
                setUpSortType(productFilterInfo.sortType)
                setUpRangeSort(productFilterInfo.rangeSort)
                setUpCategoryFiltering(productFilterInfo.filterCategory)
            }
    }

    private fun setUpCategoryFiltering(filterCategory: ProductFilterInfo.FilterCategory) {
        filterCategory.run {
            val uifilters = filters.map { filter ->
                UiFilter(
                    filter = filter, isSelected = selectedFilter?.equals(
                        filter
                    ) == true
                )
            }.toSet()
            epoxyController?.setData(uifilters)
        }

    }

    private fun setUpRangeSort(rangeSort: ProductFilterInfo.RangeSort) {
        with(binding) {
            val toCost = rangeSort.toCost?.toFloat()
            val fromCost = rangeSort.fromCost.toFloat()
            if (toCost != null) {
                rangeSliderCost.valueFrom = fromCost
                rangeSliderCost.valueTo = toCost
                rangeSliderCost.values = listOf(fromCost, toCost)
            }


            rangeSliderCost.setLabelFormatter { value: Float ->
                value.toBigDecimal().formatToPrice()
            }

            rangeSliderCost.addOnSliderTouchListener(object :
                RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    setRangeToUi(slider.valueFrom, slider.valueTo)
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    setRangeToUi(slider.valueFrom, slider.valueTo)
                    viewModel.updateRangeSort(
                        slider.valueFrom.toBigDecimal(),
                        slider.valueTo.toBigDecimal()
                    )
                }
            }
            )
        }
    }

    private fun setRangeToUi(valueFrom: Float, valueTo: Float) {
        with(binding) {
            etFrom.setText(valueFrom.toString())
            etTo.setText(valueTo.toString())
        }
    }

    private fun setUpSortType(sortType: ProductFilterInfo.SortType) {
        val btnId = when (sortType) {
            ProductFilterInfo.SortType.BY_POPULARITY -> R.id.most_popular
            ProductFilterInfo.SortType.CHEAPEST_FIRST -> R.id.cheapest
            ProductFilterInfo.SortType.MOST_EXPENSIVE_FIRST -> R.id.most_expensive
            ProductFilterInfo.SortType.DEFAULT -> null
        }
        with(binding) {
            btnId?.let {
                root.findViewById<MaterialButton>(it).isChecked = true
            }
            toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                val updated = when (checkedId) {
                    R.id.most_popular -> ProductFilterInfo.SortType.BY_POPULARITY
                    R.id.most_expensive -> ProductFilterInfo.SortType.MOST_EXPENSIVE_FIRST
                    R.id.cheapest -> ProductFilterInfo.SortType.CHEAPEST_FIRST
                    else -> ProductFilterInfo.SortType.DEFAULT
                }
                viewModel.updateSortType(updated)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
