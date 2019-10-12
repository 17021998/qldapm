package com.example.measurehearthrate.Helper

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

public class DateHelper {

    companion object {
        val FORMAT_SHORT_DATE = "dd/MM/yyy"
        private var mDefaultTimezone = TimeZone.getDefault()

        fun getCurrentDay(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(FORMAT_SHORT_DATE)
            val formatted = current.format(formatter)
            return formatted
        }

        fun formatShortDate(date: Date): String {
            val formatter = SimpleDateFormat(FORMAT_SHORT_DATE)
            return formatter.format(date)
        }
    }


}