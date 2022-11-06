package com.example.fakestore.epoxy.model


import com.example.fakestore.R
import com.example.fakestore.databinding.FilterItemBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.utils.uimanager.MainUiManager
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.UiFilter

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

            val colorId = MainUiManager.getFilterBackgroundColorId(isSelected)
            chipFilter.setChipBackgroundColorResource(colorId)
        }
    }
}
