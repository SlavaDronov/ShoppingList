package com.dron.shoppinglist.presentation

import androidx.recyclerview.widget.RecyclerView
import com.dron.shoppinglist.databinding.ItemShopListBinding
import com.dron.shoppinglist.domain.ShopItem

class ShopItemViewHolder(
    private val binding: ItemShopListBinding,
    private val onClick: (ShopItem) -> Unit,
    private val onLongClick: (ShopItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(shopItem: ShopItem) {
        binding.shopItem = shopItem
        binding.executePendingBindings()

        itemView.setOnClickListener { onClick(shopItem) }
        itemView.setOnLongClickListener {
            onLongClick(shopItem)
            true
        }
    }
}