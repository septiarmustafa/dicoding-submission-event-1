package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.common_adapter.EventAdapter
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.shared.SharedMethod

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var upcomingAdapter: EventAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerViews() {
        upcomingAdapter = EventAdapter(true){ event ->
            SharedMethod.navigateToEventDetail(this, event.id.toString())
        }
        finishedAdapter = EventAdapter(false){ event ->
            SharedMethod.navigateToEventDetail(this, event.id.toString())
        }

        binding.rvUpcomingEventsHome.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        binding.rvFinishedEventsHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = finishedAdapter
        }
    }

    private fun observeViewModel() {
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            it?.let { events ->
                if (events.isEmpty())  binding.tvUpcomingEvents.text = getString(R.string.no_upcoming_event_available) else binding.tvUpcomingEvents.text = getString(R.string.upcoming_events)
                upcomingAdapter.submitList(events)
            }
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) {
            it?.let { events ->
                if (events.isEmpty()) binding.ivEmptyState.visibility = View.VISIBLE else   binding.ivEmptyState.visibility = View.GONE
                finishedAdapter.submitList(events)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner){
            SharedMethod.showLoading(it, binding.progressBar)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                SharedMethod.showErrorDialog(
                    context = requireContext(),
                    message = it,
                    customEvent = { homeViewModel.loadAllEvent() }
                )
                homeViewModel.clearErrorMessage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}