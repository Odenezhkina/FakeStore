package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fakestore.MainViewModel
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentFavoriteBinding
import com.example.fakestore.epoxy.FavoriteItemEpoxyController
import com.example.fakestore.model.ui.UiProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding by lazy { _binding!! }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainViewModel: MainViewModel by viewModels()

        val epoxyController =
            FavoriteItemEpoxyController(mainViewModel)

        // todo reaping combine here, in ProductListFragment and DetailProductFragment

        // maybe it is better to filter first and later combine them
        combine(mainViewModel.store.stateFlow.map { it.products },
            mainViewModel.store.stateFlow.map { it.favoriteProductsIds }) { listProducts, listFavorites ->
            listProducts.map { product ->
                UiProduct(product = product, isInFavorites = listFavorites.contains(product.id))
            }.filter { it.isInFavorites }
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) { products ->
            epoxyController.setData(products)
            with(binding){
                tvGoToCatalog.isVisible = products.isEmpty()
                tvNoProductTitle.isVisible = products.isEmpty()
            }
        }
        mainViewModel.refreshProducts()

        with(binding) {
            rvFavorite.layoutManager = GridLayoutManager(requireContext(), 2)
            rvFavorite.setController(epoxyController)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
