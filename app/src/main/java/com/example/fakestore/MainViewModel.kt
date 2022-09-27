package com.example.fakestore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.domain.Product
import com.example.fakestore.redux.ApplicationState
import com.example.fakestore.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    val store: Store<ApplicationState>,
    private val productRepository: ProductRepository) : ViewModel() {

    fun refreshProducts() = viewModelScope.launch {
        val products: List<Product> = productRepository.fetchAllProducts()
        store.update { applicationState ->
            return@update applicationState.copy(products = products)
        }
    }

    fun updateFavoriteSet(changedId: Int) {
        viewModelScope.launch {
            store.update { applicationState ->
                // if contains -> remove
                // it not -> add
                var newSet: MutableSet<Int>
                applicationState.favorites.run {
                    newSet = this.toMutableSet()
                    if (contains(changedId)) {
                        newSet.remove(changedId)
                    } else {
                        newSet.add(changedId)
                    }
                }
               newSet.forEach {  Log.d("TAGTAG", "elem: $it") }
                applicationState.copy(favorites = newSet)
            }
        }
    }

}
