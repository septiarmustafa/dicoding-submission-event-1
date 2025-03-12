package com.example.dicodingevent.shared

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.dicodingevent.R
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
}