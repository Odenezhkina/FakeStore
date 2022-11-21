package com.example.fakestore.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.example.fakestore.epoxy.listeners.GeneralProductClickListener
import com.example.fakestore.model.ui.UiProduct

private val callback = object : ItemCallback<UiProduct>() {

    override fun areItemsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean {
        return oldItem == newItem // todo
    }
}

class UiProductAdapter : ListAdapter<UiProduct, UiProductViewHolder>(callback) {
    var btnListener: GeneralProductClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiProductViewHolder {
        return UiProductViewHolder.create(parent, btnListener)
    }

    override fun onBindViewHolder(holder: UiProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
