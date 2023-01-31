package com.example.fakestore.presentation.util.ext

fun <T> Set<T>.addIfContainsRemove(item: T) = toMutableSet().apply {
    if (contains(item)) {
        remove(item)
    } else {
        add(item)
    }
}
