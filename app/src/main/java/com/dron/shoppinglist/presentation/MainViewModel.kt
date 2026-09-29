package com.dron.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _shopList = MutableLiveData<List<ShopItem>>()
    val shopList: LiveData<List<ShopItem>> = _shopList

    fun getShopList() {
        viewModelScope.launch {
            val list = getShopListUseCase.getShopList()
            _shopList.value = list
        }
    }

    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
            getShopList()
        }
    }

    fun changeEnabledState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
        getShopList()
    }


    fun editShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            editShopItemUseCase.editShopItem(shopItem)
        }
    }
}