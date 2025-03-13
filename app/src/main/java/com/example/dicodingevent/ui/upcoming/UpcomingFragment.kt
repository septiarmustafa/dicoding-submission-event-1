package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.ui.adapter.EventAdapter
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.di.AppContainer
import com.example.dicodingevent.di.ViewModelFactory
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.shared.SharedMethod
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding
    private val appContainer by lazy { AppContainer(requireContext()) }
    private val upcomingViewModel by viewModels<UpcomingViewModel> {
        ViewModelFactory(appContainer.eventRepository)
    }
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        observeViewModel()

        return binding?.root
    }

    private fun setupRecyclerViews() {
        adapter = EventAdapter(false){ event ->
            SharedMethod.navigateToEventDetail(this, event.id.toString())
        }

        binding?.apply {
            rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
            rvUpcoming.adapter = adapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            upcomingViewModel.upcomingEvents.collect { result ->
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
                            customEvent = { result.let { upcomingViewModel.getUpcomingEvents() } }
                        )
                    }
                }
            }
        }

        upcomingViewModel.getUpcomingEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}