package com.dron.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        val shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.shopItemFragmentContainer,
                    ShopItemFragment.newInstance(shopItemId)
                )
            }
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