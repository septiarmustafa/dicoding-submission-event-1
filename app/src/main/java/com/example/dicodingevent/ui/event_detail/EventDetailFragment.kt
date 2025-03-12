package com.example.dicodingevent.ui.event_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.favorite_event.FavoriteEvent
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.databinding.FragmentEventDetailBinding
import com.example.dicodingevent.shared.SharedMethod
import com.example.dicodingevent.ui.favorite.FavoriteViewModel
import com.example.dicodingevent.utils.DateUtils
import kotlinx.coroutines.launch

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding
    private val viewModel: EventDetailViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var eventId: String? = null
    private var isFavorite: Boolean = false
    private var currentEvent: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventId = arguments?.getString(EVENT_ID_KEY)
        eventId?.let {
            setupObservers()
            viewModel.getEventDetail(it)
        } ?:   SharedMethod.showErrorDialog(
            context = requireContext(),
            message = "Event ID is missing",
            customEvent = { eventId?.let { viewModel.getEventDetail(it) } }
        )

        binding?.ivLoveButton?.setOnClickListener {
            currentEvent?.let { event ->
                val favoriteEvent = FavoriteEvent(
                    event.id ?: -1,
                    event.name ?: "",
                    event.summary ?: "",
                    event.imageLogo ?: ""
                )
                if (isFavorite) {
                    favoriteViewModel.removeFavorite(favoriteEvent)
                    SharedMethod.showToastTop(requireContext(), "Removed from favorites")
                } else {
                    favoriteViewModel.addFavorite(favoriteEvent)
                    SharedMethod.showToastTop(requireContext(), "Added to favorites")
                }
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding?.let { SharedMethod.showLoading(isLoading, it.progressBar) }
            }

            viewModel.event.observe(viewLifecycleOwner) { event ->
                event?.let {
                    bindEventData(it)
                    currentEvent = it

                    binding?.btnOpenLink?.setOnClickListener { it ->
                        it?.let {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.toString()))
                            startActivity(intent)
                        } ?: Toast.makeText(context, "Link not available", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
                message?.let {
                    SharedMethod.showErrorDialog(
                        context = requireContext(),
                        message = it,
                        customEvent = { eventId?.let { id -> viewModel.getEventDetail(id) } }
                    )
                    viewModel.clearErrorMessage()
                }
            }

            eventId?.let { id ->
                favoriteViewModel.isFavorite(id).observe(viewLifecycleOwner) { isFav ->
                    isFavorite = isFav
                    updateFavoriteIcon(isFav)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindEventData(event: Event) {
        binding?.apply {
            val quota = event.quota ?: 0
            val registrants = event.registrants ?: 0
            tvEventTitle.text = event.name
            tvEventOwner.text = event.ownerName
            tvEventCategory.text = event.category
            tvEventCity.text = event.cityName
            tvEventDescription.text = event.summary
            tvEventTime.text = "${DateUtils.formatToId(event.beginTime ?: "")} - ${DateUtils.formatToId(event.endTime ?: "")}"
            tvEventQuota.text =  "Quota: ${quota - registrants}"
            tvEventDescription.text = HtmlCompat.fromHtml(event.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)

            Glide.with(this@EventDetailFragment)
                .load(event.mediaCover)
                .error(R.drawable.ic_launcher_background)
                .into(ivEventImage)
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding?.ivLoveButton?.setImageResource(R.drawable.ic_favorite_24)
        } else {
            binding?.ivLoveButton?.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EVENT_ID_KEY = "EVENT_ID"
    }
}