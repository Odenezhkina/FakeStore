package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentFavoriteBinding
import com.example.fakestore.epoxy.controllers.FavoriteItemEpoxyController
import com.example.fakestore.states.FavFragmentUiState
import com.example.fakestore.viewmodels.ProductListViewModel
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
        val viewModel: ProductListViewModel by activityViewModels()
        val epoxyController = FavoriteItemEpoxyController(viewModel, findNavController())

        viewModel.uiProductReducer.reduce(viewModel.store).distinctUntilChanged().asLiveData()
            .observe(viewLifecycleOwner) { listUiProducts ->

                val favProducts = listUiProducts.filter { it.isInFavorites }

                epoxyController.setData(
                    if (favProducts.isEmpty()) FavFragmentUiState.Empty else FavFragmentUiState.NonEmpty(
                        favProducts
                    )
                )

                with(binding) {
                    rvFavorite.layoutManager = GridLayoutManager(requireContext(), 2)
                    rvFavorite.setController(epoxyController)
                }
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
