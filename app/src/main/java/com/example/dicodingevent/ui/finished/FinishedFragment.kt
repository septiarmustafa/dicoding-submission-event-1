package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dicodingevent.common_adapter.EventAdapter
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.shared.SharedMethod

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel by viewModels<FinishedViewModel>()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(true) { event ->
            val action = FinishedFragmentDirections.actionNavigationFinishedToEventDetailFragment(
                event.id.toString()
            )
            findNavController().navigate(action)
        }

        binding.rvFinishedEvents.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvFinishedEvents.adapter = adapter
    }

    private fun setupSearchView() {
        binding.svEventSearch.setOnClickListener {
            binding.svEventSearch.isIconified = false
            binding.svEventSearch.requestFocus()
        }

        binding.svEventSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                finishedViewModel.getEvent(newText)
                return true
            }
        })
    }

    private fun observeViewModel() {
        finishedViewModel.listEvent.observe(viewLifecycleOwner) {
            it?.let { events ->
                if (events.isEmpty()) binding.ivEmptyState.visibility = View.VISIBLE else   binding.ivEmptyState.visibility = View.GONE
                adapter.submitList(events)
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            SharedMethod.showLoading(it, binding.progressBar)
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                SharedMethod.showErrorDialog(
                    context = requireContext(),
                    message = it,
                    customEvent = { finishedViewModel.getEvent() }
                )
                finishedViewModel.clearErrorMessage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.svEventSearch?.setOnClickListener(null)
        _binding = null
    }
}