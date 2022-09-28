package com.example.fakestore.model.ui

import com.example.fakestore.model.domain.Filter

data class UiFilter(
    val filter: Filter,
    val isSelected: Boolean = false
)
