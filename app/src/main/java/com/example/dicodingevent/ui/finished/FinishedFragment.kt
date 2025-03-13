package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dicodingevent.ui.adapter.EventAdapter
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.di.AppContainer
import com.example.dicodingevent.di.ViewModelFactory
import com.example.dicodingevent.shared.SharedMethod
import com.example.dicodingevent.data.remote.response.Result
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding
    private val appContainer by lazy { AppContainer(requireContext()) }
    private val finishedViewModel: FinishedViewModel by viewModels {
        ViewModelFactory(appContainer.eventRepository)
    }
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        return binding?.root
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(true) { event ->
            SharedMethod.navigateToEventDetail(this, event.id.toString())
        }

        binding?.apply {
            rvFinishedEvents.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvFinishedEvents.adapter = adapter
        }
    }

    private fun setupSearchView() {
        binding?.apply {
            svEventSearch.setOnClickListener {
                svEventSearch.isIconified = false
                svEventSearch.requestFocus()
            }

            svEventSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    finishedViewModel.getFinishedEvents(newText)
                    return true
                }
            })
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            finishedViewModel.finishedEvents.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        binding?.let { SharedMethod.showLoading(true, it.progressBar) }
                    }
                    is Result.Success -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        val eventList = result.data
                        binding?.apply {
                            ivEmptyState.visibility = if (eventList.isEmpty()) View.VISIBLE else View.GONE
                            adapter.submitList(eventList)
                        }
                    }
                    is Result.Error -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        SharedMethod.showErrorDialog(
                            context = requireContext(),
                            message = result.error,
                            customEvent = { finishedViewModel.getFinishedEvents() }
                        )
                    }
                }
            }
        }

        finishedViewModel.getFinishedEvents()
//        finishedViewModel.apply {
//            listEvent.observe(viewLifecycleOwner) { events ->
//                binding?.apply {
//                    ivEmptyState.visibility = if (events.isNullOrEmpty()) View.VISIBLE else View.GONE
//                    adapter.submitList(events)
//                }
//            }
//
//            isLoading.observe(viewLifecycleOwner) { isLoading ->
//                binding?.let { SharedMethod.showLoading(isLoading, it.progressBar) }
//            }
//
//            errorMessage.observe(viewLifecycleOwner) { message ->
//                message?.let {
//                    SharedMethod.showErrorDialog(
//                        context = requireContext(),
//                        message = it,
//                        customEvent = { finishedViewModel.getEvent() }
//                    )
//                    finishedViewModel.clearErrorMessage()
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}