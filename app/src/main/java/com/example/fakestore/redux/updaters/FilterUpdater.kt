package com.example.fakestore.redux.updaters

import com.example.fakestore.model.domain.Filter
import com.example.fakestore.redux.ApplicationState
import javax.inject.Inject

class FilterUpdater @Inject constructor() {

    fun update(applicationState: ApplicationState, filter: Filter): ApplicationState {
        // if it already selected -> remove
        // else -> replace
        var newFilter: Filter? = filter
        if (applicationState.productFilterInfo.filterCategory.selectedFilter == filter) {
            newFilter = null
        }
        return applicationState.copy(
            productFilterInfo = ApplicationState.ProductFilterInfo(
                filterCategory = ApplicationState.ProductFilterInfo.FilterCategory(filters = applicationState.productFilterInfo.filterCategory.filters,
                    selectedFilter = newFilter)
            )
        )
    }
}
