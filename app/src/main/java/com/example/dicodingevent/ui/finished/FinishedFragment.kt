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
            SharedMethod.navigateToEventDetail(this, event.id.toString())
        }

        binding.apply {
            rvFinishedEvents.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvFinishedEvents.adapter = adapter
        }
    }

    private fun setupSearchView() {
        binding.apply {
            svEventSearch.setOnClickListener {
                svEventSearch.isIconified = false
                svEventSearch.requestFocus()
            }

            svEventSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    finishedViewModel.getEvent(newText)
                    return true
                }
            })
        }
    }

    private fun observeViewModel() {
        finishedViewModel.apply {
            listEvent.observe(viewLifecycleOwner) { events ->
                binding.apply {
                    ivEmptyState.visibility = if (events.isNullOrEmpty()) View.VISIBLE else View.GONE
                    adapter.submitList(events)
                }
            }

            isLoading.observe(viewLifecycleOwner) {
                SharedMethod.showLoading(it, binding.progressBar)
            }

            errorMessage.observe(viewLifecycleOwner) { message ->
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.svEventSearch?.setOnClickListener(null)
        _binding = null
    }
}