package com.example.fakestore.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fakestore.R
import com.example.fakestore.databinding.ProductListFiltersLayoutBinding

class ProductListFilterFragment : Fragment(R.layout.product_list_filters_layout) {
    private var _binding: ProductListFiltersLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProductListFiltersLayoutBinding.bind(view)

        with(binding) {
            // if checked -> change filter status (filterUpdater)
            // by filtering list in view model
            toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                when(checkedId){
                    R.id.btn_most_popular ->{

                    }
                    R.id.btn_cheapest ->{

                    }
                    R.id.btn_most_expensive ->{

                    }
                }
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
