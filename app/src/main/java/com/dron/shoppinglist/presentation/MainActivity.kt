package com.dron.shoppinglist.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dron.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var recyclerViewShopList: RecyclerView

    private val shopListAdapter = ShopListAdapter(
        onShopItemClickListener = { shopItem ->
            val intent = ShopItemActivity.newIntentEditItem(this, shopItem.id)
            startActivity(intent)
        },
        onShopItemLongClickListener = { shopItem ->
            viewModel.changeEnabledState(shopItem)
        }
    )

    private fun openAddScreen() {
        val intent = ShopItemActivity.newIntentAddItem(this)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerViewShopList = findViewById(R.id.recyclerViewShopList)
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

        setupRecyclerView()
        setupSwipeToDelete()
        setupFab()
    }

    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.buttonAddShopItem)
        fab.setOnClickListener {
            openAddScreen()
        }
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
                if (position == RecyclerView.NO_POSITION) return

                val shopItem = shopListAdapter.currentList.getOrNull(position) ?: return
                viewModel.deleteShopItem(shopItem)
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerViewShopList)
    }
}