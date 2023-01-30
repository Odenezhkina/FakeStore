package com.example.fakestore.domain.model

data class Filter(
    // title must be unique
    val title: String,
    val displayedTitle: String
)
