package com.dron.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class ShopListAdapter(
    private val onShopItemClickListener: (ShopItem) -> Unit,
    private val onShopItemLongClickListener: (ShopItem) -> Unit
) : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutId = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_list
            VIEW_TYPE_DISABLED -> R.layout.item_shop_list_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ShopItemViewHolder(
            itemView = view,
            viewType = viewType,
            onClick = onShopItemClickListener,
            onLongClick = onShopItemLongClickListener
        )
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.bind(shopItem)
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if (shopItem.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }
}