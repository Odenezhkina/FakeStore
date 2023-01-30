package com.example.fakestore.presentation.model

import com.example.fakestore.domain.model.Filter

data class UiFilter(
    val filter: Filter,
    val isSelected: Boolean = false
)
