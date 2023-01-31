package com.example.fakestore.domain.updaters

import com.example.fakestore.presentation.util.ext.addIfContainsRemove
import com.example.fakestore.presentation.ApplicationState
import javax.inject.Inject

class FavUpdater @Inject constructor() {

    fun update(applicationState: ApplicationState, productId: Int): ApplicationState {
        // if contains -> remove
        // it not -> add
        val newSet = applicationState.favoriteProductsIds.addIfContainsRemove(productId)
        return applicationState.copy(favoriteProductsIds = newSet)
    }
}
