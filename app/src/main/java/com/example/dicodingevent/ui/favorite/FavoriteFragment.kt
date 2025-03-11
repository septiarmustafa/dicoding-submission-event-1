package com.example.dicodingevent.ui.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        adapter = FavoriteAdapter { event ->
            viewModel.removeFavorite(event)
        }
        binding.rvFavoriteEvents.layoutManager = LinearLayoutManager(context)
        binding.rvFavoriteEvents.adapter = adapter

        viewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            binding.ivEmptyState.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.rvFavoriteEvents?.adapter = null
        _binding = null
    }
}