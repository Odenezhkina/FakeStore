package com.example.fakestore.redux

import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val favoriteProductsIds: Set<Int> = emptySet(),
    val productFilterInfo: ProductFilterInfo = ProductFilterInfo()
) {
    // separate class because we want to store filters (which will be displayed in the carousel)
    // and filter which is selected
    // we store it in ApplicationState because we use it here
    data class ProductFilterInfo(
        val filters: Set<Filter> = emptySet(),
        val selectedFilter: Filter? = null
    )

}
