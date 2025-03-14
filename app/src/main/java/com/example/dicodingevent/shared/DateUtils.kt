package com.example.dicodingevent.shared

import java.text.SimpleDateFormat
import java.util.Locale

class DateUtils {
    companion object {
        private const val DEFAULT_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private const val ID_OUTPUT_FORMAT = "dd MMM yyyy HH:mm"
        private const val EN_OUTPUT_FORMAT = "MMMM dd, yyyy HH:mm"
        private val ID_LOCALE = Locale("id", "ID")
        private val EN_LOCALE = Locale("en", "US")

        private fun convert(
            dateTime: String,
            locale: Locale = ID_LOCALE,
            outputDate: String
        ): String {
            return try {
                val inputDateFormat = SimpleDateFormat(DEFAULT_INPUT_FORMAT, Locale.getDefault())
                val outputDateFormat = SimpleDateFormat(outputDate, locale)
                val date = inputDateFormat.parse(dateTime)
                if (date != null) outputDateFormat.format(date) else dateTime
            } catch (e: Exception) {
                dateTime
            }
        }

        fun formatToId(date: String) : String{
           return convert(dateTime = date, outputDate = ID_OUTPUT_FORMAT)
        }

        fun formatToEn(date: String): String {
            return convert(dateTime = date, locale = EN_LOCALE, outputDate = EN_OUTPUT_FORMAT )
        }
    }
}