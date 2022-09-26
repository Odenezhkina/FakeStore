package com.example.fakestore.uicontrollers

import com.example.fakestore.MainViewModel
import com.example.fakestore.R

class UiController {
    // object because all fun is static
companion object{
        fun resIdFavorite(isInFavorites: Boolean): Int {
            return if (isInFavorites) {
                R.drawable.ic_round_favorite_24
            } else {
                R.drawable.ic_round_favorite_border_24
            }
        }

        fun onFavoriteClick(viewModel: MainViewModel, productId: Int) {
            // change icon(solid favorite) + change color
            // save changed state
            viewModel.updateFavoriteSet(productId)
        }
    }

    private fun function(){

    }
}
