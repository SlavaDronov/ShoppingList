package com.dron.shoppinglist.data

import com.dron.shoppinglist.domain.ShopItem
import com.dron.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    override suspend fun addShopItem(shopItem: ShopItem) {
        val newItem = shopItem.copy(id = autoIncrementId++)
        shopList.add(newItem)
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        shopList.add(shopItem)
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }
            ?: throw IllegalArgumentException("Item with id=$shopItemId not found")
    }

    override suspend fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }
}