package com.dron.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dron.shoppinglist.R
import com.dron.shoppinglist.databinding.FragmentShopItemBinding
import com.dron.shoppinglist.domain.ShopItem

class ShopItemFragment : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    // ← НОВОЕ: ссылка на Activity через интерфейс
    private var messageHost: MessageHost? = null

    private val isInsideShopItemActivity: Boolean
        get() = requireActivity() is ShopItemActivity

    // ← НОВОЕ: получаем ссылку при прикреплении
    override fun onAttach(context: Context) {
        super.onAttach(context)
        messageHost = context as? MessageHost
            ?: throw RuntimeException(
                "${context.javaClass.simpleName} must implement MessageHost"
            )
    }

    // ← НОВОЕ: обнуляем при откреплении
    override fun onDetach() {
        super.onDetach()
        messageHost = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopItemId = arguments?.getInt(ARG_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
            ?: ShopItem.UNDEFINED_ID
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (shopItemId != ShopItem.UNDEFINED_ID) {
            val shopItem = viewModel.getShopItem(shopItemId)
            binding.etName.setText(shopItem.name)
            binding.etCount.setText(shopItem.count.toString())
        }

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            val count = binding.etCount.text?.toString()?.toIntOrNull() ?: 0

            // Валидация
            var hasError = false
            if (name.isBlank()) {
                binding.tilName.error = getString(R.string.error_empty_name)
                hasError = true
            } else {
                binding.tilName.error = null
            }
            if (count <= 0) {
                binding.tilCount.error = getString(R.string.error_empty_count)
                hasError = true
            } else {
                binding.tilCount.error = null
            }

            if (hasError) return@setOnClickListener

            // Сохранение
            if (shopItemId == ShopItem.UNDEFINED_ID) {
                viewModel.addShopItem(ShopItem(name = name, count = count))
            } else {
                val existing = viewModel.getShopItem(shopItemId)
                val updated = existing.copy(name = name, count = count)
                viewModel.editShopItem(updated)
            }

            // ← НОВОЕ: сообщение через Activity
            messageHost?.showMessage("Сохранено")

            // Логика после сохранения
            if (isInsideShopItemActivity) {
                requireActivity().finish()
            } else {
                viewModel.stopAdding()
                viewModel.selectShopItem(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SHOP_ITEM_ID = "shop_item_id"

        fun newInstance(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}