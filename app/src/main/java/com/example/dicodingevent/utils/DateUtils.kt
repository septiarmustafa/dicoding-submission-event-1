package com.example.dicodingevent.utils

import java.text.SimpleDateFormat
import java.util.Locale

class DateUtils {
    companion object {
        private const val DEFAULT_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private const val DEFAULT_OUTPUT_FORMAT = "dd MMM yyyy HH:mm"
        private val DEFAULT_LOCALE = Locale("id", "ID")
        private fun convert(
            dateTime: String,
            locale: Locale = DEFAULT_LOCALE
        ): String {
            return try {
                val inputDateFormat = SimpleDateFormat(DEFAULT_INPUT_FORMAT, Locale.getDefault())
                val outputDateFormat = SimpleDateFormat(DEFAULT_OUTPUT_FORMAT, locale)
                val date = inputDateFormat.parse(dateTime)
                if (date != null) outputDateFormat.format(date) else dateTime
            } catch (e: Exception) {
                dateTime
            }
        }

        fun formatToId(date: String) : String{
           return convert(dateTime = date)
        }

        fun isAfter(endTime: String?): Boolean {
            return try {
                val currentTime = System.currentTimeMillis()
                val format = SimpleDateFormat(DEFAULT_INPUT_FORMAT, Locale.getDefault())
                val eventEndTime = format.parse(endTime)
                (eventEndTime?.time ?: 0) <= currentTime
            } catch (e: Exception) {
                false
            }
        }
    }
}