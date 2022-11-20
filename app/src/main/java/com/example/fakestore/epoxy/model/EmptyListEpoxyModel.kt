package com.example.fakestore.epoxy.model

import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentListEmptyStateBinding
import com.example.fakestore.epoxy.ViewBindingKotlinModel

class EmptyListEpoxyModel(private val tvToCatalogClickListener: () -> Unit) :
    ViewBindingKotlinModel<FragmentListEmptyStateBinding>(R.layout.fragment_list_empty_state) {

    override fun FragmentListEmptyStateBinding.bind() {
        tvGoToCatalog.setOnClickListener {
            tvToCatalogClickListener()
        }
    }
}
