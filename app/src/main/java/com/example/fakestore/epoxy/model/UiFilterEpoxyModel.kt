package com.example.fakestore.epoxy.model


import com.example.fakestore.R
import com.example.fakestore.databinding.FilterBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.uimanager.ProductListUiManager

class UiFilterEpoxyModel(
    private val uiFilter: UiFilter,
    private val onFilterClickListener: (Filter) -> Unit
) :
    ViewBindingKotlinModel<FilterBinding>(R.layout.filter) {
    override fun FilterBinding.bind() {
        uiFilter.run {
            tvTitle.text = filter.displayedTitle
            cardview.cardBackgroundColor
            cardview.setCardBackgroundColor(ProductListUiManager.getFilterBackgroundColorId(isSelected))
            cardview.setOnClickListener {
                onFilterClickListener(filter)
            }

            cardview.setOnClickListener {
                onFilterClickListener(filter)
            }
            Log.d("TAGTAG", "isSelected $isSelected")
            cardview.setBackgroundResource(ProductListUiManager.getFilterBackgroundColorId(uiFilter.isSelected))
        }
    }
}
