package com.example.fakestore.presentation.filter

import android.util.Log
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.domain.model.Filter
import com.example.fakestore.presentation.model.UiFilter
import com.example.fakestore.presentation.catalog.ProductListViewModel

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
        Log.d("TAGTAG", "$javaClass : updating filter ")
        viewModel.updateSelectedFilter(filter)
    }
}
