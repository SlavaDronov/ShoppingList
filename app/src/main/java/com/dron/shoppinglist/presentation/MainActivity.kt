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
import com.dron.shoppinglist.R
import com.dron.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var linearLayoutShopList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        linearLayoutShopList = findViewById(R.id.linearLayoutShopList)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            showList(it)
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun showList(list: List<ShopItem>) {
        linearLayoutShopList.removeAllViews()
        for (shopItem in list) {
            val layoutId = if (shopItem.enabled) {
                R.layout.item_shop_list
            } else {
                R.layout.item_shop_list_disabled
            }
            val view = LayoutInflater.from(this).inflate(layoutId, linearLayoutShopList, false)

            val tvName = view.findViewById<TextView>(R.id.tvName)
                ?: view.findViewById(R.id.tvNameDisabled)
            val tvCount = view.findViewById<TextView>(R.id.tvCount)
                ?: view.findViewById(R.id.tvCountDisabled)

            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()

            view.setOnLongClickListener {
                viewModel.changeEnabledState(shopItem)
                true
            }
            linearLayoutShopList.addView(view)
        }
    }
}