package com.example.fakestore.model.domain

data class Filter(
    // title must be unique
    val title: String,
    val displayedTitle: String
)
