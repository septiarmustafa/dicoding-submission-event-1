package com.example.dicodingevent.ui.event_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.databinding.FragmentEventDetailBinding
import com.example.dicodingevent.shared.SharedMethod
import com.example.dicodingevent.utils.DateUtils

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventDetailViewModel by viewModels()
    private lateinit var eventId: String

    companion object {
        const val EVENT_ID_KEY = "EVENT_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventId = arguments?.getString(EVENT_ID_KEY) ?: ""

        setupObservers()
        viewModel.getEventDetail(eventId)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupObservers() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            event?.let {
                bindEventData(it)
                handleEventFinished(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            SharedMethod.showLoading(it, binding.progressBar)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                SharedMethod.showErrorDialog(
                    context = requireContext(),
                    message = it,
                    customEvent = { viewModel.getEventDetail(eventId) }
                )
                viewModel.clearErrorMessage()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindEventData(event: Event) {
        binding.apply {
            tvEventTitle.text = event.name
            tvEventOwner.text = event.ownerName
            tvEventCategory.text = event.category
            tvEventCity.text = event.cityName
            tvEventDescription.text = event.summary
            tvEventTime.text = "${DateUtils.formatToId(event.beginTime ?: "")} - ${DateUtils.formatToId(event.endTime ?: "")}"
            tvEventQuota.text = "Quota: ${event.registrants}/${event.quota}"
            tvEventDescription.text = HtmlCompat.fromHtml(event.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)

            Glide.with(this@EventDetailFragment)
                .load(event.mediaCover)
                .error(R.drawable.ic_launcher_background)
                .into(ivEventImage)
        }
    }

    private fun handleEventFinished(event: Event) {
        val isEventFinished = DateUtils.isAfter(event.endTime)
        binding.btnOpenLink.apply {
            if (isEventFinished) {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        androidx.appcompat.R.color.material_grey_600
                    )
                )
                setOnClickListener {
                    Toast.makeText(context, "The event has ended", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (!isEventFinished) {
            binding.btnOpenLink.setOnClickListener {
                event.link?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    startActivity(intent)
                } ?: Toast.makeText(context, "Link not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}