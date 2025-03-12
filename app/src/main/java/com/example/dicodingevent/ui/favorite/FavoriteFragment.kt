package com.example.dicodingevent.ui.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(requireActivity().application)
    }
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        adapter = FavoriteAdapter { event ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.removeFavorite(event)
            }
        }
        binding.rvFavoriteEvents.layoutManager = LinearLayoutManager(context)
        binding.rvFavoriteEvents.adapter = adapter

        viewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            if (events.isNullOrEmpty()) {
                binding.ivEmptyState.visibility = View.VISIBLE
            } else {
                binding.ivEmptyState.visibility = View.GONE
            }
            adapter.submitList(events)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.rvFavoriteEvents?.adapter = null
        _binding = null
    }
}