package com.example.fakestore.uimanager

import android.util.Log
import com.example.fakestore.MainViewModel
import com.example.fakestore.R

class UiManager {
    // object because all fun is static
companion object{
        fun resIdFavorite(isInFavorites: Boolean): Int {
            return if (isInFavorites) {
                R.drawable.ic_round_favorite_24
            } else {
                R.drawable.ic_round_favorite_border_24
            }
        }
    }

    private fun function(){

    }
}
