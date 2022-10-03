package com.example.fakestore.redux.updaters

import com.example.fakestore.redux.ApplicationState
import javax.inject.Inject

class CartUpdater @Inject constructor() {

    fun update(
        applicationState: ApplicationState,
        productId: Int
    ): ApplicationState {
        return applicationState.copy(
            productCartInfo = applicationState.productCartInfo.update(
                productId,
            )
        )
    }
}
