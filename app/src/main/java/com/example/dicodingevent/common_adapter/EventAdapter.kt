package com.example.dicodingevent.common_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.databinding.ItemEventHorizontalBinding
import com.example.dicodingevent.databinding.ItemEventVerticalBinding

class EventAdapter(private val isVerticalItem: Boolean, private val onItemClick: (Event) -> Unit) : ListAdapter<Event, EventAdapter.EventViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return if (isVerticalItem) {
            val binding = ItemEventVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            EventViewHolder.FinishedViewHolder(binding)
        } else {
            val binding = ItemEventHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            EventViewHolder.UpcomingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    sealed class EventViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(event: Event, onClick: (Event) -> Unit)

        class FinishedViewHolder(private val binding: ItemEventVerticalBinding) : EventViewHolder(binding) {
            override fun bind(event: Event, onClick: (Event) -> Unit) {
                binding.tvEventTitle.text = event.name
                binding.tvEventDescription.text = event.summary
                Glide.with(binding.root.context)
                    .load(event.imageLogo)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.ivEventImage)

                itemView.setOnClickListener {
                    onClick(event)
                }
            }
        }

        class UpcomingViewHolder(private val binding: ItemEventHorizontalBinding) : EventViewHolder(binding) {
            override fun bind(event: Event, onClick: (Event) -> Unit) {
                binding.tvEventTitle.text = event.name
                binding.tvEventDescription.text = event.summary
                Glide.with(binding.root.context)
                    .load(event.imageLogo)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.ivEventImage)

                itemView.setOnClickListener {
                    onClick(event)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }
}
