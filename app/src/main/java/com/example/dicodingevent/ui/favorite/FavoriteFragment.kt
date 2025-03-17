package com.example.dicodingevent.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.di.AppContainer
import com.example.dicodingevent.di.ViewModelFactory
import com.example.dicodingevent.shared.SharedMethod
import com.example.dicodingevent.ui.adapter.FavoriteAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding
    private val appContainer by lazy { AppContainer(requireContext()) }
    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory(appContainer.eventRepository, requireActivity().application)
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
            if (events.isNullOrEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(events)
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}