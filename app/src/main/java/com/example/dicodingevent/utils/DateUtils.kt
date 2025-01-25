package com.example.dicodingevent.utils

import java.text.SimpleDateFormat
import java.util.Locale

class DateUtils {
    companion object {
        private fun convert(
            dateTime: String,
            inputFormat: String? = "yyyy-MM-dd HH:mm:ss" ,
            outputFormat: String? = "dd MMM yyyy HH:mm",
            locale: Locale = Locale("id", "ID")
        ): String {
            return try {
                val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
                val outputDateFormat = SimpleDateFormat(outputFormat, locale)
                val date = inputDateFormat.parse(dateTime)
                if (date != null) outputDateFormat.format(date) else dateTime
            } catch (e: Exception) {
                dateTime
            }
        }

        fun formatToId(date: String) : String{
           return convert(
                dateTime = date
            )
        }

        fun isAfter(endTime: String?): Boolean {
            return try {
                val currentTime = System.currentTimeMillis()
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val eventEndTime = format.parse(endTime)
                (eventEndTime?.time ?: 0) <= currentTime
            } catch (e: Exception) {
                false
            }
        }
    }
}