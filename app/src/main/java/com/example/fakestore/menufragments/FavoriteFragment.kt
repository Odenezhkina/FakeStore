package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentFavoriteBinding
import com.example.fakestore.epoxy.controllers.FavoriteItemEpoxyController
import com.example.fakestore.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding by lazy { _binding!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        val viewModel: MainViewModel by activityViewModels()
        val epoxyController = FavoriteItemEpoxyController(viewModel, findNavController())

        viewModel.uiProductReducer.reduce(viewModel.store).distinctUntilChanged().asLiveData()
            .observe(viewLifecycleOwner) { listUiProducts ->

                val favProducts = listUiProducts.filter { it.isInFavorites }
                epoxyController.setData(favProducts)
                manageUi(favProducts.isEmpty(), epoxyController)
            }
    }

    private fun manageUi(isProductsEmpty: Boolean, epoxyController: FavoriteItemEpoxyController) {
        with(binding) {
            rvFavorite.layoutManager = GridLayoutManager(requireContext(), 2)
            rvFavorite.setController(epoxyController)

            tvGoToCatalog.isVisible = isProductsEmpty
            tvNoProductTitle.isVisible = isProductsEmpty
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
