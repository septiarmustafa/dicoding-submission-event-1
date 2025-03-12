package com.example.dicodingevent.shared

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dicodingevent.ui.favorite.FavoriteFragment
import com.example.dicodingevent.ui.favorite.FavoriteFragmentDirections
import com.example.dicodingevent.ui.finished.FinishedFragment
import com.example.dicodingevent.ui.finished.FinishedFragmentDirections
import com.example.dicodingevent.ui.home.HomeFragment
import com.example.dicodingevent.ui.home.HomeFragmentDirections
import com.example.dicodingevent.ui.upcoming.UpcomingFragment
import com.example.dicodingevent.ui.upcoming.UpcomingFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object SharedMethod {
    fun showLoading(isLoading: Boolean, progressBar: View) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun showErrorDialog(
        context: Context,
        title: String? = "Error",
        labelNo: String? = "Back",
        labelYes: String? = "Retry",
        message: String,
        customEvent: (() -> Unit)? = null

    ) {
        val dialogBuilder = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(labelNo, null)

        customEvent?.let {
            dialogBuilder.setPositiveButton(labelYes) { _, _ -> it() }
        }

        dialogBuilder.show()
    }

    fun navigateToEventDetail(fragment: Fragment, eventId: String) {
        val navController = fragment.findNavController()

        val action = when (fragment) {
            is HomeFragment -> HomeFragmentDirections.actionNavigationHomeToEventDetailFragment(eventId)
            is FavoriteFragment -> FavoriteFragmentDirections.actionNavigationFavoriteToEventDetailFragment(eventId)
            is UpcomingFragment -> UpcomingFragmentDirections.actionNavigationUpcomingToEventDetailFragment(eventId)
            is FinishedFragment -> FinishedFragmentDirections.actionNavigationFinishedToEventDetailFragment(eventId)
            else -> null
        }

        action?.let { navController.navigate(it) }
    }
}