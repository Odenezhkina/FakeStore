package com.example.fakestore.epoxy.controllers

import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.epoxy.model.UiFilterEpoxyModel
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.viewmodels.ProductListViewModel

class UiFilterItemController(private val viewModel: ProductListViewModel) :
    TypedEpoxyController<Set<UiFilter>>() {

    override fun buildModels(data: Set<UiFilter>?) {
        // todo  handle empty state
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
