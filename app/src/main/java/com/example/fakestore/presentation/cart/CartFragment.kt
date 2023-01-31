package com.example.fakestore.presentation.cart

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.fakestore.databinding.FragmentCartBinding
import com.example.fakestore.presentation.util.epoxy.decorators.SimpleVerticalDividerItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding by lazy { _binding!! }

    private val viewModel: CartViewModel by viewModels()
    private var epoxyController: CartProductEpoxyController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        initObservers()
    }

    private fun initObservers() {
        epoxyController = CartProductEpoxyController(viewModel, findNavController())
        epoxyController?.setFilterDuplicates(true)
        epoxyController?.let { binding.rvCart.setController(it) }

        viewModel.cartProductReducer.reduce(viewModel.store)
            .distinctUntilChanged()
            .asLiveData()
            .observe(viewLifecycleOwner) { cartUiProductList ->
                val viewState = if (cartUiProductList.isEmpty()) {
                    CartFragmentUiState.Empty
                } else {
                    CartFragmentUiState.NonEmpty(cartUiProductList)
                }
                epoxyController?.setData(viewState)
            }
    }

    private fun setupRecycler() {
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
                setupEpoxyItemTouchHelper(viewModel)
            }
        }
    }

    private fun setupEpoxyItemTouchHelper(viewModel: CartViewModel) {
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
