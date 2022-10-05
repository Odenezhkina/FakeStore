package com.example.fakestore.menufragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentCartBinding
import com.example.fakestore.epoxy.controllers.CartProductEpoxyController
import com.example.fakestore.model.ui.CartUiProduct
import com.example.fakestore.viewmodels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding by lazy { _binding!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    private fun initObservers() {
        val viewModel: CartViewModel by viewModels()
        val epoxyController = CartProductEpoxyController(viewModel)
        viewModel.uiProductDetailedReducer.reduce(viewModel.store)
            .distinctUntilChanged()
            .asLiveData()
            .observe(viewLifecycleOwner) { cartUiProductList ->
                manageUi(cartUiProductList, epoxyController)
            }

    }

    private fun manageUi(cartUiProductList: List<CartUiProduct>, epoxyController: CartProductEpoxyController) {
        with(binding) {
            // todo fix logic
            tvGoToCatalog.isVisible = cartUiProductList.isEmpty()
            tvNoProductTitle.isVisible =  cartUiProductList.isEmpty()

            epoxyController.setData(cartUiProductList)
            rvCart.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvCart.setController(epoxyController)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
