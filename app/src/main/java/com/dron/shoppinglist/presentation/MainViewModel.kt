package com.dron.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dron.shoppinglist.data.ShopListRepositoryImpl
import com.dron.shoppinglist.domain.AddShopItemUseCase
import com.dron.shoppinglist.domain.DeleteShopItemUseCase
import com.dron.shoppinglist.domain.EditShopItemUseCase
import com.dron.shoppinglist.domain.GetShopItemUseCase   // ← добавить импорт
import com.dron.shoppinglist.domain.GetShopListUseCase
import com.dron.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)  // ← добавить

    val shopList: LiveData<List<ShopItem>> = getShopListUseCase.getShopList()

    private val _selectedShopItem = MutableLiveData<ShopItem?>()
    val selectedShopItem: LiveData<ShopItem?> = _selectedShopItem

    var isTwoPane: Boolean = false

    fun selectShopItem(shopItem: ShopItem?) {
        _selectedShopItem.value = shopItem
    }

    fun getShopItem(shopItemId: Int): ShopItem {   // ← добавить
        return getShopItemUseCase.getShopItem(shopItemId)
    }

    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
            if (_selectedShopItem.value?.id == shopItem.id) {
                _selectedShopItem.value = null
            }
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

    fun addShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            addShopItemUseCase.addShopItem(shopItem)
        }
    }
}