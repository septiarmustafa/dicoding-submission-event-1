package com.example.dicodingevent.ui.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.shared.SharedMethod

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding
    private val viewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(requireActivity().application)
    }
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        adapter = FavoriteAdapter(
            onDeleteClick = { event ->
                viewModel.removeFavorite(event)
            },
            onItemClick = { event ->
                SharedMethod.navigateToEventDetail(this, event.id.toString())
            }
        )
        binding?.apply {
            rvFavoriteEvents.layoutManager = LinearLayoutManager(context)
            rvFavoriteEvents.adapter = adapter
        }


        viewModel.favoriteEventsEntity.observe(viewLifecycleOwner) { events ->
            if (events.isNullOrEmpty()) {
                binding?.ivEmptyState?.visibility = View.VISIBLE
            } else {
                binding?.ivEmptyState?.visibility = View.GONE
            }
            adapter.submitList(events)
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}