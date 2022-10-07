package com.example.fakestore.redux.generator

import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product
import javax.inject.Inject

class FilterGenerator @Inject constructor() {

    fun generateFilters(productList: List<Product>): Set<Filter> {
        return productList
            .groupBy { it.category }
            .map { mapEntry ->
                Filter(title = mapEntry.key, displayedTitle = mapEntry.key)
            }.toSet()
    }
}
