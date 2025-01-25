package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.common_adapter.EventAdapter
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.shared.SharedMethod

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val upcomingViewModel by viewModels<UpcomingViewModel>()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerViews() {
        adapter = EventAdapter(false){ event ->
            val action = UpcomingFragmentDirections.actionNavigationUpcomingToEventDetailFragment(
                event.id.toString()
            )
            findNavController().navigate(action)
        }

        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.adapter = adapter
    }

    private fun observeViewModel() {
        upcomingViewModel.listEvent.observe(viewLifecycleOwner) {
            it?.let { events ->
                if (events.isEmpty()) binding.ivEmptyState.visibility = View.VISIBLE else   binding.ivEmptyState.visibility = View.GONE
                adapter.submitList(events)
            }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner){
            SharedMethod.showLoading(it, binding.progressBar)
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                SharedMethod.showErrorDialog(
                    context = requireContext(),
                    message = it,
                    customEvent = { upcomingViewModel.getEvent() }
                )
                upcomingViewModel.clearErrorMessage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}