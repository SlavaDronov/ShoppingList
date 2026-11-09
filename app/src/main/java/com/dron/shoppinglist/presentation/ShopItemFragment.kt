package com.dron.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dron.shoppinglist.R
import com.dron.shoppinglist.databinding.FragmentShopItemBinding
import com.dron.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch

class ShopItemFragment : Fragment() {

    private val tag = "FragmentLifecycle"

    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    private var messageHost: MessageHost? = null

    private val isInsideShopItemActivity: Boolean
        get() = requireActivity() is ShopItemActivity

    // ========== ЖИЗНЕННЫЙ ЦИКЛ ==========

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(tag, "onAttach() — фрагмент прикреплён к Activity")
        messageHost = context as? MessageHost
            ?: throw RuntimeException("${context.javaClass.simpleName} must implement MessageHost")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate() savedInstanceState=${savedInstanceState != null}")
        shopItemId = arguments?.getInt(ARG_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
            ?: ShopItem.UNDEFINED_ID
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(tag, "onCreateView() — создаём View-иерархию")
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(tag, "onViewCreated() — View создан, можно вешать слушатели")

        if (shopItemId != ShopItem.UNDEFINED_ID) {
            viewLifecycleOwner.lifecycleScope.launch {
                val shopItem = viewModel.getShopItem(shopItemId)
                if (shopItem != null) {
                    binding.etName.setText(shopItem.name)
                    binding.etCount.setText(shopItem.count.toString())
                }
            }
        }
        setupClickListener()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(tag, "onViewStateRestored() — состояние View восстановлено")
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "onStart() — фрагмент видим")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume() — фрагмент в фокусе, можно взаимодействовать")
    }

    override fun onPause() {
        Log.d(tag, "onPause() — фрагмент теряет фокус")
        super.onPause()
    }

    override fun onStop() {
        Log.d(tag, "onStop() — фрагмент больше не видим")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag, "onSaveInstanceState() — сохраняем состояние")
    }

    override fun onDestroyView() {
        Log.d(tag, "onDestroyView() — View уничтожается")
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy() — фрагмент уничтожается")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(tag, "onDetach() — фрагмент откреплён от Activity")
        super.onDetach()
        messageHost = null
    }

    // ========== ЛОГИКА ==========

    private fun setupClickListener() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            val count = binding.etCount.text?.toString()?.toIntOrNull() ?: 0

            var hasError = false
            if (name.isBlank()) {
                binding.tilName.error = getString(R.string.error_empty_name)
                hasError = true
            } else binding.tilName.error = null

            if (count <= 0) {
                binding.tilCount.error = getString(R.string.error_empty_count)
                hasError = true
            } else binding.tilCount.error = null

            if (hasError) return@setOnClickListener

            if (shopItemId == ShopItem.UNDEFINED_ID) {
                viewModel.addShopItem(ShopItem(name = name, count = count))
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val existing = viewModel.getShopItem(shopItemId)
                    if (existing != null) {
                        viewModel.editShopItem(existing.copy(name = name, count = count))
                    }
                }
            }

            messageHost?.showMessage("Сохранено")

            if (isInsideShopItemActivity) {
                requireActivity().finish()
            } else {
                viewModel.stopAdding()
                viewModel.selectShopItem(null)
            }
        }
    }

    companion object {
        private const val ARG_SHOP_ITEM_ID = "shop_item_id"

        fun newInstance(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply { putInt(ARG_SHOP_ITEM_ID, shopItemId) }
            }
        }
    }
}