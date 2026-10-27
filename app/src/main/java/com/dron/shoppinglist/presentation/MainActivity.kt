package com.dron.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var currentShopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.isTwoPane = findViewById<View>(R.id.shopItemContainer) != null
        Log.d("MainActivity", "isTwoPane = ${viewModel.isTwoPane}")

        if (savedInstanceState != null) {
            viewModel.selectShopItem(null)
            supportFragmentManager.fragments.toList().forEach { fragment ->
                supportFragmentManager.beginTransaction().remove(fragment).commitNow()
            }
        }

        setupFragments()
        observeSelectedItem()
        observeAddingMode()   // ← НОВОЕ
        setupBackPressed()
    }

    private fun setupFragments() {
        if (viewModel.isTwoPane) {
            addFragmentIfNeeded(R.id.shopListContainer) { ShopListFragment() }
        } else {
            addFragmentIfNeeded(android.R.id.content) { ShopListFragment() }
        }
    }

    private fun observeSelectedItem() {
        if (!viewModel.isTwoPane) return

        viewModel.selectedShopItem.observe(this) { shopItem ->
            // Если включён режим добавления — не реагируем на selectedShopItem
            if (viewModel.isAddingMode.value == true) return@observe

            val fragment = supportFragmentManager.findFragmentById(R.id.shopItemContainer)

            if (shopItem != null) {
                if (currentShopItemId == shopItem.id && fragment != null) return@observe

                currentShopItemId = shopItem.id
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(
                        R.id.shopItemContainer,
                        ShopItemFragment.newInstance(shopItem.id)
                    )
                }
            } else {
                currentShopItemId = ShopItem.UNDEFINED_ID
                if (fragment != null) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        remove(fragment)
                    }
                }
            }
        }
    }

    // ← НОВЫЙ метод: обрабатывает FAB (режим добавления)
    private fun observeAddingMode() {
        if (!viewModel.isTwoPane) return

        viewModel.isAddingMode.observe(this) { isAdding ->
            if (isAdding) {
                // Показать пустую форму
                currentShopItemId = ShopItem.UNDEFINED_ID
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(
                        R.id.shopItemContainer,
                        ShopItemFragment.newInstance(ShopItem.UNDEFINED_ID)
                    )
                }
            }
            // Когда isAdding = false — форма управляется observeSelectedItem
        }
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isTwoPane) {
                    val shopItemFragment = supportFragmentManager
                        .findFragmentById(R.id.shopItemContainer) as? ShopItemFragment

                    if (shopItemFragment != null) {
                        viewModel.stopAdding()        // ← НОВОЕ
                        viewModel.selectShopItem(null)
                        return
                    }
                }

                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        })
    }

    private fun addFragmentIfNeeded(containerId: Int, factory: () -> Fragment) {
        Log.d("MainActivity", "containerId=$containerId, exists=${supportFragmentManager.findFragmentById(containerId) != null}")
        if (supportFragmentManager.findFragmentById(containerId) == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(containerId, factory())
            }
        }
    }
}