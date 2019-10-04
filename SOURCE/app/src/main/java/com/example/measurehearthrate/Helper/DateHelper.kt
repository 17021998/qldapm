package com.example.measurehearthrate.Helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

public class DateHelper {

    companion object {
        val FORMAT_SHORT_DATE = "yyyy-MM-dd"
        fun getCurrentDay(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            return formatted
        }
    }


}