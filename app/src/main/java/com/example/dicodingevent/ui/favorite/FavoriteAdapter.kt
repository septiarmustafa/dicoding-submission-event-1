package com.example.dicodingevent.ui.favorite


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.favorite_event.FavoriteEvent
import com.example.dicodingevent.databinding.ItemFavoriteEventBinding

class FavoriteAdapter(
    private val onDeleteClick: (FavoriteEvent) -> Unit,
    private val onItemClick: (FavoriteEvent) -> Unit
) : ListAdapter<FavoriteEvent, FavoriteAdapter.FavoriteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteEventBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEvent) {
            binding.apply {
                tvEventTitle.text = event.title
                tvEventSubtitle.text = event.description
                Glide.with(ivEventImage.context)
                    .load(event.imageUrl)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivEventImage)

                root.setOnClickListener { onItemClick(event) }
                ivDeleteFavorite.setOnClickListener { onDeleteClick(event) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FavoriteEvent>() {
        override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem == newItem
        }
    }
}