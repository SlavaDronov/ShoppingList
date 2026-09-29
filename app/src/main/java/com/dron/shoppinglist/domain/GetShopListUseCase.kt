package com.dron.shoppinglist.domain

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun getShopList(): List<ShopItem> {
        return shopListRepository.getShopList()
    }
}