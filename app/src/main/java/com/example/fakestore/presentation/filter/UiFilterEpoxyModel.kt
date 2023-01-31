package com.example.fakestore.presentation.filter


import com.example.fakestore.R
import com.example.fakestore.databinding.FilterItemBinding
import com.example.fakestore.domain.model.Filter
import com.example.fakestore.presentation.model.UiFilter
import com.example.fakestore.presentation.util.ViewBindingKotlinModel
import com.example.fakestore.presentation.util.ext.setSelectedStyle

class UiFilterEpoxyModel(
    private val uiFilter: UiFilter,
    private val onFilterClickListener: (Filter) -> Unit
) :
    ViewBindingKotlinModel<FilterItemBinding>(R.layout.filter_item) {
    override fun FilterItemBinding.bind() {
        uiFilter.run {
            chipFilter.text = filter.displayedTitle
            chipFilter.setOnClickListener {
                onFilterClickListener(filter)
            }
            // todo listener isSelected
            chipFilter.setSelectedStyle(isSelected)
        }
    }
}
