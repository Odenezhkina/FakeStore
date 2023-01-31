package com.example.fakestore.presentation.util.ext

fun <K, V> Map<K, V>.addIfContainsRemove(key: K, value: V) = toMutableMap().apply {
    if (contains(key)) {
        remove(key)
    } else {
        put(key, value)
    }
}
