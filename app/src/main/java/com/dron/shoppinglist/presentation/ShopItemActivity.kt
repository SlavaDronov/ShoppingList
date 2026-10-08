package com.dron.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dron.shoppinglist.R
import com.dron.shoppinglist.databinding.ActivityShopItemBinding
import com.dron.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding

    private val viewModel: ShopItemViewModel by viewModels()

    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        observeViewModel()
        setupClickListener()
    }

    private fun parseIntent() {
        if (intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
        if (shopItemId != ShopItem.UNDEFINED_ID) {
            val shopItem = viewModel.getShopItem(shopItemId)
            binding.etName.setText(shopItem.name)
            binding.etCount.setText(shopItem.count.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.errorName.observe(this) { hasError ->
            binding.tilName.error = if (hasError) {
                getString(R.string.error_empty_name)
            } else {
                null
            }
        }

        viewModel.errorCount.observe(this) { hasError ->
            binding.tilCount.error = if (hasError) {
                getString(R.string.error_empty_count)
            } else {
                null
            }
        }

        viewModel.shouldCloseScreen.observe(this) { shouldClose ->
            if (shouldClose) finish()
        }
    }

    private fun setupClickListener() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            val count = binding.etCount.text?.toString()?.toIntOrNull() ?: 0
            viewModel.saveShopItem(name, count, shopItemId)
        }
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"

        fun newIntentAddItem(context: Context): Intent {
            return Intent(context, ShopItemActivity::class.java)
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            return Intent(context, ShopItemActivity::class.java).apply {
                putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            }
        }
    }
}