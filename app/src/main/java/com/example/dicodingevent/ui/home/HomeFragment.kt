package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.ui.adapter.EventAdapter
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.di.AppContainer
import com.example.dicodingevent.di.ViewModelFactory
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.shared.SharedMethod
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val appContainer by lazy { AppContainer(requireContext()) }
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(appContainer.eventRepository)
    }
    private lateinit var upcomingAdapter: EventAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        observeViewModel()

        return binding?.root
    }

    private fun setupRecyclerViews() {
        upcomingAdapter = EventAdapter(true) { event ->
            SharedMethod.navigateToEventDetail(this, event.id.toString())
        }
        finishedAdapter = EventAdapter(false) { event ->
            SharedMethod.navigateToEventDetail(this, event.id.toString())
        }

        binding?.apply {
            rvUpcomingEventsHome.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = upcomingAdapter
            }

            rvFinishedEventsHome.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = finishedAdapter
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.upcomingEvents.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        binding?.let { SharedMethod.showLoading(true, it.progressBar) }
                    }

                    is Result.Success -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        val eventList = result.data
                        binding?.tvUpcomingEvents?.text = if (eventList.isEmpty()) {
                            getString(R.string.no_upcoming_event_available)
                        } else {
                            getString(R.string.upcoming_events)
                        }
                        upcomingAdapter.submitList(eventList)
                    }

                    is Result.Error -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        SharedMethod.showErrorDialog(
                            context = requireContext(),
                            message = result.error,
                            customEvent = { homeViewModel.loadAllEvents() }
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.finishedEvents.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        binding?.let { SharedMethod.showLoading(true, it.progressBar) }
                    }

                    is Result.Success -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        val eventList = result.data
                        binding?.ivEmptyState?.visibility =
                            if (eventList.isEmpty()) View.VISIBLE else View.GONE
                        finishedAdapter.submitList(eventList)
                    }

                    is Result.Error -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        SharedMethod.showErrorDialog(
                            context = requireContext(),
                            message = result.error,
                            customEvent = { homeViewModel.loadAllEvents() }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}