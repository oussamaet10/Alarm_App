package com.example.alarm

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Alarm(
    val id: Int,
    val title: String,
    val hour: Int,
    val minute: Int,
    var isEnabled: Boolean = true,
    val days: List<Boolean> = List(7) { true }  // Represents days of week
) {
    fun getFormattedTime(): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.time)
    }

    fun getFormattedDays(): String {
        val daysCharArray = "SMTWTFS".toCharArray()
        return daysCharArray.filterIndexed { index, _ -> days[index] }.joinToString(" ")
    }
}