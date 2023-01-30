package com.example.fakestore.presentation.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductListFiltersLayoutBinding
import com.example.fakestore.presentation.model.UiFilter
import com.example.fakestore.presentation.ApplicationState
import com.example.fakestore.utils.uimanager.MainUiManager.formatToPrice
import com.example.fakestore.presentation.catalog.ProductListViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

            // 1) get fresh data and set it to ui
            // 2) update data
            // 3) get fresh data

            initObservers()
//            setUpSortType(viewModel.store.stateFlow.value.productFilterInfo.sortType)
//            setUpRangeSort(viewModel.store.stateFlow.value.productFilterInfo.rangeSort)
//            initObservers()
        }
    }
    private fun initObservers2() {
        lifecycleScope.launch {
            viewModel.store.stateFlow
                .map { it.productFilterInfo }
                .distinctUntilChanged().last().run{
                    setUpSortType(sortType)
                    setUpRangeSort(rangeSort)
                    setUpCategoryFiltering(filterCategory)
                }
        }
    }

    private fun initObservers() {
        viewModel.store.stateFlow
            .map { it.productFilterInfo }
            .distinctUntilChanged()
            .asLiveData()
            .observe(viewLifecycleOwner) { productFilterInfo ->

                Log.d("TAGTAG", "$javaClass : observing filter ")
                setUpSortType(productFilterInfo.sortType)
                setUpRangeSort(productFilterInfo.rangeSort)
                setUpCategoryFiltering(productFilterInfo.filterCategory)

            }
    }

    private fun setUpCategoryFiltering(filterCategory: ApplicationState.ProductFilterInfo.FilterCategory) {
        filterCategory.run {
            val uifilters = filters.map { filter ->
                UiFilter(
                    filter = filter,
                    isSelected = selectedFilter?.equals(
                        filter
                    ) == true
                )
            }.toSet()
            epoxyController?.setData(uifilters)
        }

    }

    private fun setUpRangeSort(rangeSort: ApplicationState.ProductFilterInfo.RangeSort) {
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

    private fun setUpSortType(sortType: ApplicationState.ProductFilterInfo.SortType) {
        sortType.sortTypeId?.let {
            binding.root.findViewById<MaterialButton>(sortType.sortTypeId).isChecked = true
        }
        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            viewModel.updateSortType(checkedId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
