package com.dron.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.dron.shoppinglist.databinding.ItemShopListBinding
import com.dron.shoppinglist.domain.ShopItem

class ShopListAdapter(
    private val onShopItemClickListener: (ShopItem) -> Unit,
    private val onShopItemLongClickListener: (ShopItem) -> Unit
) : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = ItemShopListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopItemViewHolder(
            binding = binding,
            onClick = onShopItemClickListener,
            onLongClick = onShopItemLongClickListener
        )
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}