package com.dron.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {
    var count = 0

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter", "log: ${++count}")
        val layoutId = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_list
            VIEW_TYPE_DISABLED -> R.layout.item_shop_list_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.bind(shopItem)

    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if (shopItem.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    inner class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvName: TextView = itemView.findViewById(R.id.tvName)
            ?: itemView.findViewById(R.id.tvNameDisabled)
        private val tvCount: TextView = itemView.findViewById(R.id.tvCount)
            ?: itemView.findViewById(R.id.tvCountDisabled)

        fun bind(shopItem: ShopItem) {
            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()

            itemView.setOnClickListener {
                onShopItemClickListener?.invoke(shopItem)

            }
            itemView.setOnLongClickListener {
                onShopItemLongClickListener?.invoke(shopItem)
                true
            }
        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }

    class ShopItemDiffCallback : DiffUtil.ItemCallback<ShopItem>() {

        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem == newItem
        }
    }
}