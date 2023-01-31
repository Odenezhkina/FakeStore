package com.example.fakestore.presentation.filter

import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.domain.model.Filter
import com.example.fakestore.presentation.catalog.ProductListViewModel
import com.example.fakestore.presentation.model.UiFilter

class UiFilterItemController(private val viewModel: ProductListViewModel) :
    TypedEpoxyController<Set<UiFilter>>() {

    override fun buildModels(data: Set<UiFilter>?) {
        data?.forEach {
            UiFilterEpoxyModel(
                it,
                ::onFilterClickListener
            ).id(it.filter.title).addTo(this)
        }
    }

    private fun onFilterClickListener(filter: Filter) {
        viewModel.updateSelectedFilter(filter)
    }
}
