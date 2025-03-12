package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.common_adapter.EventAdapter
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.shared.SharedMethod

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding
    private val upcomingViewModel by viewModels<UpcomingViewModel>()
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

        binding?.rvUpcoming?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.adapter
        }
    }

    private fun observeViewModel() {
        upcomingViewModel.apply {
            listEvent.observe(viewLifecycleOwner) { events ->
                binding?.apply {
                    ivEmptyState.visibility = if (events.isNullOrEmpty()) View.VISIBLE else View.GONE
                    adapter.submitList(events)
                }
            }

            isLoading.observe(viewLifecycleOwner) {
                binding?.progressBar?.let { progressBar -> SharedMethod.showLoading(it, progressBar) }
            }

            errorMessage.observe(viewLifecycleOwner) { message ->
                message?.let {
                    SharedMethod.showErrorDialog(
                        context = requireContext(),
                        message = it,
                        customEvent = { getEvent() }
                    )
                    clearErrorMessage()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}