package com.example.fakestore.presentation.util.ext

import com.example.fakestore.R
import com.google.android.material.chip.Chip

fun Chip.setSelectedStyle(isSelected: Boolean) {
    val colorId = if (isSelected) R.color.orange else R.color.dark_blue
    setChipBackgroundColorResource(colorId)
}
