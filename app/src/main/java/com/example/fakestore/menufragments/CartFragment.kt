package com.example.fakestore.menufragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.fakestore.R
import com.example.fakestore.databinding.FragmentCartBinding
import com.example.fakestore.epoxy.controllers.CartProductEpoxyController
import com.example.fakestore.epoxy.decorators.SimpleVerticalDividerItemDecorator
import com.example.fakestore.epoxy.model.CartProductEpoxyModel
import com.example.fakestore.states.CartFragmentUiState
import com.example.fakestore.viewmodels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding by lazy { _binding!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

        initObservers()
    }

    private fun initObservers() {
        val viewModel: CartViewModel by viewModels()

        val epoxyController = CartProductEpoxyController(viewModel, findNavController())
        epoxyController.setFilterDuplicates(true)
        binding.rvCart.setController(epoxyController)

        viewModel.cartProductReducer.reduce(viewModel.store)
            .distinctUntilChanged()
            .asLiveData()
            .observe(viewLifecycleOwner) { cartUiProductList ->
                val viewState = if (cartUiProductList.isEmpty()) {
                    CartFragmentUiState.Empty
                } else {
                    CartFragmentUiState.NonEmpty(cartUiProductList)
                }
                epoxyController.setData(viewState)
            }

        with(binding) {
            rvCart.run {
                if (!isDirty) {
                    addItemDecoration(
                        SimpleVerticalDividerItemDecorator(
                            MARGIN_BOTTOM_RECYCLER_VIEW_ITEM
                        )
                    )
                }
                layoutManager =
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                setUpEpoxyItemTouchHelper(viewModel)
                rvCart.setController(epoxyController)
            }

        }

    }

    private fun setUpEpoxyItemTouchHelper(viewModel: CartViewModel) {
        EpoxyTouchHelper
            .initSwiping(binding.rvCart)
            .right()
            .withTarget(CartProductEpoxyModel::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<CartProductEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: CartProductEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    model?.let { epoxyModel ->
                        val id: Int = epoxyModel.cartProduct.uiProduct.product.id
                        viewModel.updateCartProductsId(id)
                    }
                }

                override fun onSwipeProgressChanged(
                    model: CartProductEpoxyModel?,
                    itemView: View?,
                    swipeProgress: Float,
                    canvas: Canvas?
                ) {
                    return
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MARGIN_BOTTOM_RECYCLER_VIEW_ITEM = 16
    }
}
