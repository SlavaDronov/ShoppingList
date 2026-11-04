package com.dron.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), MessageHost {

    private val tag = "ShopItemActivityLifecycle"

    // ========== ЖИЗНЕННЫЙ ЦИКЛ ==========

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate(savedInstanceState=${savedInstanceState != null})")
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

    override fun onStart()   { super.onStart();   Log.d(tag, "onStart()") }
    override fun onResume()  { super.onResume();  Log.d(tag, "onResume()") }
    override fun onPause()   { Log.d(tag, "onPause()");   super.onPause() }
    override fun onStop()    { Log.d(tag, "onStop()");    super.onStop() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag, "onSaveInstanceState()")
    }

    override fun onDestroy() { Log.d(tag, "onDestroy()"); super.onDestroy() }

    // ========== MessageHost ==========

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"

        fun newIntentAddItem(context: Context): Intent =
            Intent(context, ShopItemActivity::class.java)

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent =
            Intent(context, ShopItemActivity::class.java).apply {
                putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            }
    }
}