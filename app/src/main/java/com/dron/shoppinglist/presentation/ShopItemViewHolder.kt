package com.dron.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class ShopItemViewHolder(
    itemView: View,
    viewType: Int,
    private val onClick: (ShopItem) -> Unit,
    private val onLongClick: (ShopItem) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val tvName: TextView
    private val tvCount: TextView

    init {
        if (viewType == ShopListAdapter.VIEW_TYPE_ENABLED) {
            tvName = itemView.findViewById(R.id.tvName)
            tvCount = itemView.findViewById(R.id.tvCount)
        } else {
            tvName = itemView.findViewById(R.id.tvNameDisabled)
            tvCount = itemView.findViewById(R.id.tvCountDisabled)
        }
    }

    fun bind(shopItem: ShopItem) {
        tvName.text = shopItem.name
        tvCount.text = shopItem.count.toString()

        itemView.setOnClickListener { onClick(shopItem) }
        itemView.setOnLongClickListener {
            onLongClick(shopItem)
            true
        }
    }
}