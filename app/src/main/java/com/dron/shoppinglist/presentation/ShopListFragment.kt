package com.dron.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dron.shoppinglist.databinding.FragmentShopListBinding

class ShopListFragment : Fragment() {

    private var _binding: FragmentShopListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val shopListAdapter = ShopListAdapter(
        onShopItemClickListener = { shopItem ->
            android.util.Log.d("ShopListFragment", "clicked, isTwoPane=${viewModel.isTwoPane}, item=${shopItem.name}")
            if (viewModel.isTwoPane) {
                viewModel.selectShopItem(shopItem)
            } else {
                val intent = ShopItemActivity.newIntentEditItem(requireContext(), shopItem.id)
                startActivity(intent)
            }
        },
        onShopItemLongClickListener = { shopItem ->
            viewModel.changeEnabledState(shopItem)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.post {
            Log.d("ShopListFragment", "list root size: ${view.width}x${view.height}")
            Log.d("ShopListFragment", "list parent: ${view.parent}")
        }

        setupRecyclerView()
        setupSwipeToDelete()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewShopList.adapter = shopListAdapter
        binding.recyclerViewShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
        binding.recyclerViewShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_DISABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return

                val shopItem = shopListAdapter.currentList.getOrNull(position) ?: return
                viewModel.deleteShopItem(shopItem)
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerViewShopList)
    }

    private fun setupFab() {
        binding.buttonAddShopItem.setOnClickListener {
            if (viewModel.isTwoPane) {
                viewModel.selectShopItem(null)  // пустая форма справа
            } else {
                val intent = ShopItemActivity.newIntentAddItem(requireContext())
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.shopList.observe(viewLifecycleOwner) {
            shopListAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}