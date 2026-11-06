package com.dron.shoppinglist.presentation

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dron.shoppinglist.BR
import com.dron.shoppinglist.domain.ShopItem

class ShopItemViewHolder(
    private val binding: ViewDataBinding,
    private val onClick: (ShopItem) -> Unit,
    private val onLongClick: (ShopItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(shopItem: ShopItem) {
        binding.setVariable(BR.shopItem, shopItem)
        binding.executePendingBindings()

        itemView.setOnClickListener { onClick(shopItem) }
        itemView.setOnLongClickListener {
            onLongClick(shopItem)
            true
        }
    }
}