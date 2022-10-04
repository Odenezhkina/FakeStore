package com.example.fakestore.epoxy.model


import android.content.res.ColorStateList
import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import com.example.fakestore.R
import com.example.fakestore.databinding.FilterBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.uimanager.MainUiManager

class UiFilterEpoxyModel(
    private val res: Resources,
    private val uiFilter: UiFilter,
    private val onFilterClickListener: (Filter) -> Unit
) :
    ViewBindingKotlinModel<FilterBinding>(R.layout.filter) {
    override fun FilterBinding.bind() {
        uiFilter.run {
            chipFilter.text = filter.displayedTitle
            chipFilter.setOnClickListener {
                onFilterClickListener(filter)
            }
            // todo listener isSelected

            val colorId = MainUiManager.getFilterBackgroundColorId(isSelected)
            chipFilter.setChipBackgroundColorResource(colorId)
            chipFilter.backgroundTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(res, colorId, null)
            )
        }
    }
}
