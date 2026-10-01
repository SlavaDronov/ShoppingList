package com.dron.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dron.shoppinglist.data.ShopListRepositoryImpl
import com.dron.shoppinglist.domain.DeleteShopItemUseCase
import com.dron.shoppinglist.domain.EditShopItemUseCase
import com.dron.shoppinglist.domain.GetShopListUseCase
import com.dron.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList: LiveData<List<ShopItem>> = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnabledState(shopItem: ShopItem) {
        viewModelScope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }
    }

    fun editShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            editShopItemUseCase.editShopItem(shopItem)
        }
    }
}