package com.example.fakestore.presentation.util

import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentListEmptyStateBinding

class EmptyListEpoxyModel(private val tvToCatalogClickListener: () -> Unit) :
    ViewBindingKotlinModel<FragmentListEmptyStateBinding>(R.layout.fragment_list_empty_state) {

    override fun FragmentListEmptyStateBinding.bind() {
        tvGoToCatalog.setOnClickListener {
            tvToCatalogClickListener()
        }
    }
}
