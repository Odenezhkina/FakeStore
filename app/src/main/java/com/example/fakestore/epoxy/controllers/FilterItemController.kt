package com.example.fakestore.epoxy.controllers

import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.epoxy.model.UiFilterEpoxyModel
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.viewmodels.MainViewModel

class FilterItemController(private val viewModel: MainViewModel) :
    TypedEpoxyController<List<UiFilter>>() {

    override fun buildModels(data: List<UiFilter>?) {
        // todo  handle empty state
        data?.let {
            val uiFilterModels = data.map { uifilter ->
                UiFilterEpoxyModel(
                    uiFilter = uifilter,
                    onFilterClickListener = ::onFilterClickListener
                ).id(uifilter.filter.title)
            }
            CarouselModel_().models(uiFilterModels).id("").addTo(this)
        }
    }

    private fun onFilterClickListener(filter: Filter) {
        viewModel.updateSelectedFilter(filter)
    }
}
