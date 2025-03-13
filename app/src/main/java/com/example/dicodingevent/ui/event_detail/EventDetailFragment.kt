package com.example.dicodingevent.ui.event_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
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
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.databinding.FragmentEventDetailBinding
import com.example.dicodingevent.di.AppContainer
import com.example.dicodingevent.di.ViewModelFactory
import com.example.dicodingevent.shared.SharedMethod
import com.example.dicodingevent.ui.favorite.FavoriteViewModel
import com.example.dicodingevent.shared.DateUtils
import kotlinx.coroutines.launch

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding
    private val appContainer by lazy { AppContainer(requireContext()) }
    private val eventDetailViewModel: EventDetailViewModel by viewModels {
        ViewModelFactory(appContainer.eventRepository)
    }
    private val favoriteViewModel: FavoriteViewModel by viewModels{
        ViewModelFactory(appContainer.eventRepository, requireActivity().application)
    }
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
            eventDetailViewModel.getEventDetail(it)
        } ?:   SharedMethod.showErrorDialog(
            context = requireContext(),
            message = "Event ID is missing",
            customEvent = { eventId?.let { eventDetailViewModel.getEventDetail(it) } }
        )

        binding?.ivLoveButton?.setOnClickListener {
            currentEvent?.let { event ->
                val favoriteEventEntity = FavoriteEventEntity(
                    event.id ?: -1,
                    event.name ?: "",
                    event.summary ?: "",
                    event.imageLogo ?: ""
                )
                if (isFavorite) {
                    favoriteViewModel.removeFavorite(favoriteEventEntity)
                    SharedMethod.showToastTop(requireContext(), "Removed from favorites")
                } else {
                    favoriteViewModel.addFavorite(favoriteEventEntity)
                    SharedMethod.showToastTop(requireContext(), "Added to favorites")
                }
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            eventDetailViewModel.event.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        binding?.let { SharedMethod.showLoading(true, it.progressBar) }
                    }
                    is Result.Success -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        result.data.let { event: Event ->
                            bindEventData(event)
                            currentEvent = event

                            binding?.btnOpenLink?.setOnClickListener {
                                val url = event.link.toString()
                                if (url.isNotEmpty() && Patterns.WEB_URL.matcher(url).matches()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Invalid or missing URL", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        binding?.let { SharedMethod.showLoading(false, it.progressBar) }
                        SharedMethod.showErrorDialog(
                            context = requireContext(),
                            message = result.error,
                            customEvent = { eventId?.let { id -> eventDetailViewModel.getEventDetail(id) } }
                        )
                    }
                }
            }
        }

        eventId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
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