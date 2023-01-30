package com.example.fakestore.domain.generator

import com.example.fakestore.domain.model.Filter
import com.example.fakestore.domain.model.Product
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
