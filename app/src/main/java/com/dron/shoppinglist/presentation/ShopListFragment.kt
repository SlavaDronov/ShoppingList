package com.dron.shoppinglist.presentation

import android.content.Context
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

    private val tag = "FragmentLifecycle"

    private var _binding: FragmentShopListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val shopListAdapter = ShopListAdapter(
        onShopItemClickListener = { shopItem ->
            Log.d("ShopListFragment", "clicked, isTwoPane=${viewModel.isTwoPane}, item=${shopItem.name}")
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

    // ========== ЖИЗНЕННЫЙ ЦИКЛ ==========

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(tag, "ShopListFragment.onAttach() — прикреплён к ${context.javaClass.simpleName}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "ShopListFragment.onCreate() savedInstanceState=${savedInstanceState != null}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(tag, "ShopListFragment.onCreateView() — создаём View")
        _binding = FragmentShopListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(tag, "ShopListFragment.onViewCreated() — View создан")

        view.post {
            Log.d("ShopListFragment", "list root size: ${view.width}x${view.height}")
            Log.d("ShopListFragment", "list parent: ${view.parent}")
        }

        setupRecyclerView()
        setupSwipeToDelete()
        setupFab()
        observeViewModel()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(tag, "ShopListFragment.onViewStateRestored() savedInstanceState=${savedInstanceState != null}")
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "ShopListFragment.onStart() — видим")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "ShopListFragment.onResume() — в фокусе")
    }

    override fun onPause() {
        Log.d(tag, "ShopListFragment.onPause() — теряет фокус")
        super.onPause()
    }

    override fun onStop() {
        Log.d(tag, "ShopListFragment.onStop() — не видим")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag, "ShopListFragment.onSaveInstanceState() — сохраняем состояние")
    }

    override fun onDestroyView() {
        Log.d(tag, "ShopListFragment.onDestroyView() — View уничтожается")
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Log.d(tag, "ShopListFragment.onDestroy() — фрагмент уничтожается")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(tag, "ShopListFragment.onDetach() — откреплён")
        super.onDetach()
    }

    // ========== ЛОГИКА ==========

    private fun setupRecyclerView() {
        binding.recyclerViewShopList.adapter = shopListAdapter
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
                viewModel.startAdding()
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
}