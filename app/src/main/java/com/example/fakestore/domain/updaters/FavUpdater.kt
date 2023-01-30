package com.example.fakestore.domain.updaters

import com.example.fakestore.presentation.ApplicationState
import javax.inject.Inject

class FavUpdater @Inject constructor() {

    fun update(applicationState: ApplicationState, productId: Int): ApplicationState {
        // if contains -> remove
        // it not -> add
        var newSet: MutableSet<Int>
        applicationState.favoriteProductsIds.run {
            newSet = this.toMutableSet()
            if (contains(productId)) {
                newSet.remove(productId)
            } else {
                newSet.add(productId)
            }
        }
        return applicationState.copy(favoriteProductsIds = newSet)
    }
}
