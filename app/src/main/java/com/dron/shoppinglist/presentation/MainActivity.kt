package com.dron.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerViewShopList: RecyclerView
    private val shopListAdapter = ShopListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerViewShopList = findViewById(R.id.recyclerViewShopList)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

        setupRecyclerView()
        setupClickListeners()
        setupSwipeToDelete()
    }

    private fun setupRecyclerView() {
        recyclerViewShopList.adapter = shopListAdapter
        recyclerViewShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
        recyclerViewShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_DISABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
    }

    private fun setupClickListeners() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnabledState(it)
        }
        shopListAdapter.onShopItemClickListener = {
            // TODO: открыть экран редактирования (следующий урок)
        }
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val shopItem = shopListAdapter.currentList[position]
                    viewModel.deleteShopItem(shopItem)
                }
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerViewShopList)
    }
}