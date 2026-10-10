package com.dron.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dron.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Определяем режим: если есть shopItemContainer — ландшафт
        val shopItemContainer = findViewById<View>(R.id.shopItemContainer)
        viewModel.isTwoPane = shopItemContainer != null
        Log.d("MainActivity", "isTwoPane = ${viewModel.isTwoPane}")

        // При повороте Android восстанавливает фрагменты,
        // но в контейнеры, которых может не быть. Удаляем их.
        if (savedInstanceState != null) {
            supportFragmentManager.fragments.toList().forEach { fragment ->
                supportFragmentManager.beginTransaction().remove(fragment).commitNow()
            }
        }

        setupFragments()
    }

    private fun setupFragments() {
        if (viewModel.isTwoPane) {
            // Ландшафт: два контейнера
            addFragmentIfNeeded(R.id.shopListContainer) { ShopListFragment() }
            addFragmentIfNeeded(R.id.shopItemContainer) { ShopItemFragment() }
        } else {
            // Портрет: один контейнер с id = android.R.id.content
            // используем контейнер по умолчанию (setContentView)
            Log.d("MainActivity", "containerId=android.R.id.content, exists=${supportFragmentManager.findFragmentById(android.R.id.content) != null}")
            if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(android.R.id.content, ShopListFragment())
                }
            }
        }
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