package com.example.fakestore.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductListFiltersLayoutBinding
import com.example.fakestore.epoxy.controllers.UiFilterItemController
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.viewmodels.MainViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ProductListFilterFragment : Fragment(R.layout.product_list_filters_layout) {
    private var _binding: ProductListFiltersLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProductListFiltersLayoutBinding.bind(view)

        with(binding) {
            val viewModel: MainViewModel by activityViewModels()
            val epoxyController =
                UiFilterItemController(viewModel)
            filtersEpoxyCarousel.setController(epoxyController)

            viewModel.store.stateFlow.map { it.productFilterInfo }
                .distinctUntilChanged()
                .asLiveData()
                .observe(viewLifecycleOwner) { productFilterInfo ->
                    val uifilters = productFilterInfo.filters.map { filter ->
                        UiFilter(
                            filter = filter,
                            isSelected = productFilterInfo.selectedFilter?.equals(filter) == true
                        )
                    }.toSet()

                    epoxyController.setData(uifilters)
                }

            // if checked -> change filter status (filterUpdater)
            // by filtering list in view model
            toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                when (checkedId) {
                    R.id.btn_most_popular -> {

                    }
                    R.id.btn_cheapest -> {

                    }
                    R.id.btn_most_expensive -> {

                    }
                }
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
