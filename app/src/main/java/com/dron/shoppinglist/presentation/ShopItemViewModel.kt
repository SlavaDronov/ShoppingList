package com.dron.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dron.shoppinglist.data.ShopListRepositoryImpl
import com.dron.shoppinglist.domain.AddShopItemUseCase
import com.dron.shoppinglist.domain.EditShopItemUseCase
import com.dron.shoppinglist.domain.GetShopItemUseCase
import com.dron.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _errorName = MutableLiveData<Boolean>()
    val errorName: LiveData<Boolean> = _errorName

    private val _errorCount = MutableLiveData<Boolean>()
    val errorCount: LiveData<Boolean> = _errorCount

    private val _shouldCloseScreen = MutableLiveData<Boolean>()
    val shouldCloseScreen: LiveData<Boolean> = _shouldCloseScreen

    fun getShopItem(shopItemId: Int): ShopItem {
        return getShopItemUseCase.getShopItem(shopItemId)
    }

    fun saveShopItem(name: String, count: Int, shopItemId: Int) {
        if (name.isBlank() || count <= 0) {
            validateInput(name, count)
            return
        }

        viewModelScope.launch {
            if (shopItemId == ShopItem.UNDEFINED_ID) {
                // Добавление — создаём новый
                val newItem = ShopItem(name = name, count = count)
                addShopItemUseCase.addShopItem(newItem)
            } else {
                // Редактирование — копируем существующий, меняем только name/count
                val existingItem = getShopItemUseCase.getShopItem(shopItemId)
                val updatedItem = existingItem.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(updatedItem)
            }
            _shouldCloseScreen.value = true
        }
    }

    private fun validateInput(name: String, count: Int) {
        _errorName.value = name.isBlank()
        _errorCount.value = count <= 0
    }
}